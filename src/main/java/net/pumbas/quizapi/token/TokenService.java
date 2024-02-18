package net.pumbas.quizapi.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.crypto.SecretKey;
import net.pumbas.quizapi.config.Configuration;
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
   * Generate a JWT that represents an access token. This access token is used to authenticate
   * requests to the API.
   *
   * @param userId The id of the user to generate an access token for
   * @return The JWT that represents the access token
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
   * Generate a JWT that represents a refresh token. This refresh token is the beginning of a new
   * token family and contains a reference to the user that it's associated with.
   *
   * @param userId The id of the user to generate a refresh token for
   * @return The JWT that represents the refresh token
   */
  public String generateNewRefreshToken(Long userId) {
    User user = this.userRepository.getReferenceById(userId);

    RefreshToken newRefreshToken = RefreshToken.builder()
        .user(user)
        .build();

    RefreshToken createdRefreshToken = this.refreshTokenRepository.save(newRefreshToken);
    return this.jwtFromRefreshToken(createdRefreshToken);
  }

  /**
   * Generate a JWT that represents a refresh token. This refresh token is a rotation of another one
   * and is part of the same token family as the refresh token that it was rotated from.
   *
   * @param refreshToken The refresh token to rotate
   * @return The JWT that represents the refresh token
   */
  public String generateRotatedRefreshToken(RefreshToken refreshToken) {
    RefreshToken originalToken = refreshToken.getOriginalToken() == null
        ? refreshToken : refreshToken.getOriginalToken();

    RefreshToken newRefreshToken = RefreshToken.builder()
        .user(refreshToken.getUser())
        .originalToken(originalToken)
        .build();

    RefreshToken createdRefreshToken = this.refreshTokenRepository.save(newRefreshToken);
    return this.jwtFromRefreshToken(createdRefreshToken);
  }

  /**
   * Generate a JWT that represents a refresh token. The JWT contains the id of the refresh token as
   * the subject which is used to verify the refresh token when it's used.
   *
   * @param refreshToken The refresh token to generate a JWT for
   * @return The JWT that represents the refresh token
   */
  private String jwtFromRefreshToken(RefreshToken refreshToken) {
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

}
