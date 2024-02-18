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

  public String generateRefreshToken(Long userId) {
    ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
    ZonedDateTime expiration = now.plusSeconds(this.configuration.getRefreshTokenExpirySeconds());

    User user = this.userRepository.getReferenceById(userId);

    RefreshToken newRefreshToken = RefreshToken.builder()
        .state(RefreshTokenState.UNUSED)
        .user(user)
        .build();

    RefreshToken createdRefreshToken = this.refreshTokenRepository.save(newRefreshToken);

    return Jwts.builder()
        .subject(createdRefreshToken.getId().toString())
        .expiration(Date.from(expiration.toInstant()))
        .issuedAt(Date.from(now.toInstant()))
        .issuer(API_ISSUER)
        .signWith(this.jwtSecret)
        .compact();
  }

}
