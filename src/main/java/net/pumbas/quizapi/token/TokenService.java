package net.pumbas.quizapi.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.crypto.SecretKey;
import net.pumbas.quizapi.config.Configuration;
import net.pumbas.quizapi.exception.NotFoundException;
import net.pumbas.quizapi.exception.UnauthorizedException;
import net.pumbas.quizapi.user.User;
import net.pumbas.quizapi.user.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  public static final String API_ISSUER = "open-quiz-api";
  private final RefreshTokenRepository refreshTokenRepository;
  private final Configuration configuration;
  private final UserService userService;
  private final SecretKey jwtSecret;
  private final Log logger = LogFactory.getLog(TokenService.class);

  public TokenService(
      RefreshTokenRepository refreshTokenRepository,
      Configuration configuration,
      UserService userService
  ) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.configuration = configuration;
    this.userService = userService;

    byte[] keyBytes = Decoders.BASE64.decode(this.configuration.getJwtSecret());
    this.jwtSecret = Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Get the refresh token with the provided id.
   *
   * @param refreshTokenId The id of the refresh token to get
   * @return The refresh token with the provided id
   * @throws UnauthorizedException If no refresh token could be found with the provided id
   */
  public RefreshToken getRefreshToken(Long refreshTokenId) {
    return this.refreshTokenRepository.findById(refreshTokenId)
        .orElseThrow(() -> new UnauthorizedException(
            "Could not find refresh token with id: " + refreshTokenId));
  }

  /**
   * Validate that the provided access token JWS is valid and that the user associated with it
   * exists.
   *
   * @param accessTokenJws The JWS that represents the access token
   * @return The user associated with the access token
   * @throws UnauthorizedException If the access token is not valid
   */
  public User validateAccessToken(String accessTokenJws) {
    Long userId = this.validateJws(accessTokenJws, "access token");
    try {
      return this.userService.getUser(userId);
    } catch (NotFoundException e) {
      throw new UnauthorizedException(
          "The user associated with the access token could not be found: " + userId, e);
    }
  }

  /**
   * Validate that the provided refresh token JWS is valid and has not been used before.
   * <p>
   * If the token has already been used ({@link RefreshTokenState#INVALIDATED_USED}), then this
   * could be a potential refresh token reuse attack and all the tokens in the same token family are
   * invalidated.
   * <p>
   * If the token has been invalidated but not used ({@link RefreshTokenState#INVALIDATED}), then
   * the token is marked as used but no other action is taken.
   *
   * @param refreshTokenJws The JWS that represents the refresh token
   * @return The refresh token entity
   * @throws UnauthorizedException If the refresh token is not valid or has already been used
   */
  public RefreshToken validateRefreshToken(String refreshTokenJws) {
    Long refreshTokenId = this.validateJws(refreshTokenJws, "refresh token");
    RefreshToken refreshToken = this.getRefreshToken(refreshTokenId);

    if (refreshToken.getState() == RefreshTokenState.INVALIDATED_USED) {
      // This token has already been used! This is a potential refresh token reuse attack!!!
      this.logger.warn("Refresh token reuse: Refresh token %s for user '%s' has already been used"
          .formatted(refreshTokenJws, refreshToken.getUser().getUsername()));

      Long originalTokenId = this.getOriginalToken(refreshToken).getId();
      this.refreshTokenRepository.invalidateRefreshTokenFamily(originalTokenId);
      throw new UnauthorizedException("The refresh token has already been used");
    }
    if (refreshToken.getState() == RefreshTokenState.INVALIDATED) {
      // We allow an invalidated token to be used once as there's no way for the frontend to know
      // it's been invalidated until it tries to use it.
      refreshToken.setState(RefreshTokenState.INVALIDATED_USED); // Mark the token as used
      this.refreshTokenRepository.save(refreshToken);
      throw new UnauthorizedException("The refresh token has already been invalidated");
    }

    refreshToken.setState(RefreshTokenState.INVALIDATED_USED);
    return this.refreshTokenRepository.save(refreshToken);
  }

  /**
   * Validate that the provided JWS is valid and hasn't expired and return the parsed id stored in
   * the subject of the JWS.
   *
   * @param jws       The JWS to validate
   * @param tokenName The name of the token to use in error messages
   * @return The id stored in the subject of the JWS
   * @throws UnauthorizedException If the JWS is not valid or has expired
   */
  private Long validateJws(String jws, String tokenName) {
    try {
      Claims claims = Jwts.parser()
          .verifyWith(this.jwtSecret)
          .build()
          .parseSignedClaims(jws)
          .getPayload();

      if (!API_ISSUER.equals(claims.getIssuer())) {
        throw new UnauthorizedException("The %s was not issued by this API".formatted(tokenName));
      }

      Date expiration = claims.getExpiration();
      if (expiration == null) {
        throw new UnauthorizedException(
            "The %s does not have an expiration date".formatted(tokenName));
      }

      // Make sure we check the expiration in UTC
      Date now = Date.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant());
      if (expiration.before(now)) {
        throw new UnauthorizedException("The %s has expired".formatted(tokenName));
      }

      return Long.parseLong(claims.getSubject());
    } catch (UnsupportedJwtException e) {
      throw new UnauthorizedException("The %s is not a valid JWS".formatted(tokenName), e);
    } catch (JwtException e) {
      throw new UnauthorizedException(
          "The %s could not be verified successfully".formatted(tokenName), e);
    } catch (NumberFormatException e) {
      throw new UnauthorizedException(
          "The subject of the %s is not a valid number".formatted(tokenName), e);
    }

  }

  /**
   * Generate a JWS that represents an access token. This access token is used to authenticate
   * requests to the API.
   *
   * @param userId The id of the user to generate an access token for
   * @return The JWS that represents the access token
   */
  public String generateAccessToken(Long userId) {
    ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
    ZonedDateTime expiration = now.plusSeconds(this.configuration.getAccessTokenExpirySeconds());

    return Jwts.builder()
        .subject(userId.toString())
        .expiration(Date.from(expiration.toInstant()))
        .issuedAt(Date.from(now.toInstant()))
        .issuer(API_ISSUER)
        .signWith(this.jwtSecret)
        .compact();
  }

  /**
   * Generate a JWS that represents a refresh token. This refresh token is the beginning of a new
   * token family and contains a reference to the user that it's associated with.
   *
   * @param userId The id of the user to generate a refresh token for
   * @return The JWS that represents the refresh token
   */
  public String generateNewRefreshToken(Long userId) {
    User user = this.userService.getUserReference(userId);

    RefreshToken newRefreshToken = RefreshToken.builder()
        .user(user)
        .build();

    RefreshToken createdRefreshToken = this.refreshTokenRepository.save(newRefreshToken);
    return this.jwsFromRefreshToken(createdRefreshToken);
  }

  /**
   * Generate a JWS that represents a refresh token. This refresh token is a rotation of another one
   * and is part of the same token family as the refresh token that it was rotated from.
   *
   * @param refreshToken The refresh token to rotate
   * @return The JWS that represents the refresh token
   */
  public String generateRotatedRefreshToken(RefreshToken refreshToken) {
    RefreshToken newRefreshToken = RefreshToken.builder()
        .user(refreshToken.getUser())
        .originalToken(this.getOriginalToken(refreshToken))
        .build();

    RefreshToken createdRefreshToken = this.refreshTokenRepository.save(newRefreshToken);
    return this.jwsFromRefreshToken(createdRefreshToken);
  }

  /**
   * Generate a JWS that represents a refresh token. The JWS contains the id of the refresh token as
   * the subject which is used to verify the refresh token when it's used.
   *
   * @param refreshToken The refresh token to generate a JWS for
   * @return The JWS that represents the refresh token
   */
  private String jwsFromRefreshToken(RefreshToken refreshToken) {
    ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
    ZonedDateTime expiration = now.plusSeconds(this.configuration.getAccessTokenExpirySeconds());

    return Jwts.builder()
        .subject(refreshToken.getUser().getId().toString())
        .expiration(Date.from(expiration.toInstant()))
        .issuedAt(Date.from(now.toInstant()))
        .issuer(API_ISSUER)
        .signWith(this.jwtSecret)
        .compact();
  }

  /**
   * Get the original refresh token. If the original token is null, then that means the refresh
   * token is the root of a token family and is the original token.
   *
   * @param refreshToken The refresh token to get the original token for
   * @return The original refresh token
   */
  private RefreshToken getOriginalToken(RefreshToken refreshToken) {
    return null == refreshToken.getOriginalToken() ? refreshToken : refreshToken.getOriginalToken();
  }

}
