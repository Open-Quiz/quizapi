package net.pumbas.quizapi.auth.providers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import net.pumbas.quizapi.config.Configuration;
import net.pumbas.quizapi.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

@Service
public class GoogleUserDataProvider implements UserDataProvider {

  private final GoogleIdTokenVerifier verifier;

  public GoogleUserDataProvider(
      Configuration configuration
  ) throws IOException, GeneralSecurityException {
    NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    this.verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        .setAudience(Collections.singletonList(configuration.getGoogle().getClientId()))
        .build();
  }

  @Override
  public UserData extractUserData(String token) {
    try {
      GoogleIdToken idToken = this.verifier.verify(token);
      if (idToken == null) {
        throw new UnauthorizedException(
            "The Google id token in the Authorization header is invalid");
      }

      Payload payload = idToken.getPayload();
      String userId = payload.getSubject();
      String name = (String) payload.get("name");
      String pictureUrl = (String) payload.get("picture");

      return new UserData(name, pictureUrl, userId);
    } catch (IOException | GeneralSecurityException e) {
      throw new UnauthorizedException(
          "There was an unexpected exception while verifying the Google id token", e);
    }
  }

}
