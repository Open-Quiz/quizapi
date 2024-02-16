package net.pumbas.quizapi.config;

import lombok.Data;
import net.pumbas.quizapi.user.providers.GoogleUserDataProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("net.pumbas")
public class Configuration {

  private Google google;

  /**
   * Configuration related to {@link GoogleUserDataProvider}.
   */
  @Data
  public static class Google {

    private String clientId;

  }

}
