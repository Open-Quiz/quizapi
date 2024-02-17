package net.pumbas.quizapi.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.crypto.SecretKey;
import net.pumbas.quizapi.config.Configuration;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  private final Configuration configuration;
  private final SecretKey jwtSecret;

  public TokenService(Configuration configuration) {
    this.configuration = configuration;
    byte[] keyBytes = Decoders.BASE64.decode(this.configuration.getJwtSecret());
    this.jwtSecret = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateAccessToken(Long userId) {
    ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
    ZonedDateTime expiration = now.plusSeconds(this.configuration.getAccessTokenExpirySeconds());

    // TODO: Add issuer and audience
    return Jwts.builder()
        .subject(userId.toString())
        .expiration(Date.from(expiration.toInstant()))
        .issuedAt(Date.from(now.toInstant()))
        .signWith(this.jwtSecret)
        .compact();
  }

  public String generateRefreshToken(Long userId) {
    return null;
  }

}
