package net.pumbas.quizapi.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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
import net.pumbas.quizapi.exception.UnauthorizedException;
import net.pumbas.quizapi.user.User;
import net.pumbas.quizapi.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  public static final String API_ISSUER = "open-quiz-api";
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;
  private final Configuration configuration;
  private final SecretKey jwtSecret;

  public TokenService(
      RefreshTokenRepository refreshTokenRepository,
      UserRepository userRepository,
      Configuration configuration
  ) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.userRepository = userRepository;
    this.configuration = configuration;
    byte[] keyBytes = Decoders.BASE64.decode(this.configuration.getJwtSecret());
    this.jwtSecret = Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Validate that the provided refresh token JWS is valid and has not been used before. If the
   * refresh token is valid then a new rotated refresh token is returned.
   * <p>
   * If the token has already been used ({@link RefreshTokenState#INVALIDATED_USED}), then this
   * could be a potential refresh token reuse attack and all the tokens in the same token family are
   * invalidated.
   * <p>
   * If the token has been invalidated but not used ({@link RefreshTokenState#INVALIDATED}), then
   * the token is marked as used but no other action is taken.
   *
   * @param refreshTokenJws The JWS that represents the refresh token
   * @return The new rotated refresh token JWS
   * @throws UnauthorizedException If the refresh token is not valid or has already been used
   */
  public String validateRefreshToken(String refreshTokenJws) {
    Long refreshTokenId = this.getRefreshTokenIdFromJws(refreshTokenJws);

    RefreshToken refreshToken = this.refreshTokenRepository.findById(refreshTokenId)
        .orElseThrow(() -> new UnauthorizedException(
            "No refresh token could not be found with the id: '%s'".formatted(refreshTokenId)));

    if (refreshToken.getState() == RefreshTokenState.INVALIDATED_USED) {
      // This token has already been used! This is a potential refresh token reuse attack!!!

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
    this.refreshTokenRepository.save(refreshToken);

    return this.generateRotatedRefreshToken(refreshToken);

  }

  /**
   * Extract the id of the refresh token from the JWS and verify that the JWS is valid and not
   * expired.
   *
   * @param refreshTokenJws The JWS that represents the refresh token
   * @return The id of the refresh token
   * @throws UnauthorizedException If the refresh token is invalid or has expired
   */
  private Long getRefreshTokenIdFromJws(String refreshTokenJws) {
    try {
      Jws<Claims> jws = Jwts.parser()
          .verifyWith(this.jwtSecret)
          .build()
          .parseSignedClaims(refreshTokenJws);

      Date expiration = jws.getPayload().getExpiration();
      if (expiration == null) {
        throw new UnauthorizedException("The refresh token does not have an expiration date");
      }

      // Make sure we check the expiration in UTC
      Date now = Date.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant());
      if (expiration.before(now)) {
        throw new UnauthorizedException("The refresh token has expired");
      }

      return Long.parseLong(jws.getPayload().getSubject());
    } catch (UnsupportedJwtException e) {
      throw new UnauthorizedException("The refresh token is not a valid JWS", e);
    } catch (JwtException e) {
      throw new UnauthorizedException("The refresh token could not be verified successfully", e);
    } catch (NumberFormatException e) {
      throw new UnauthorizedException("The id of the refresh token is not a valid number", e);
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
    User user = this.userRepository.getReferenceById(userId);

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
