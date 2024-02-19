package net.pumbas.quizapi;

import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Encoders;
import javax.crypto.SecretKey;

/**
 * This class is used to generate a base64 encoded HS512 key that can be used to sign JWTs created
 * by this API.
 */
public class JwtSecretGenerator {

  public static void main(String[] args) {
    SecretKey key = SIG.HS512.key().build();
    String encodedKey = Encoders.BASE64.encode(key.getEncoded());
    System.out.println("Base64 encoded HS512 Key:");
    System.out.println(encodedKey);
  }

}
