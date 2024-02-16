package net.pumbas.quizapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("net.pumbas")
public class Configuration {

  private Google google;

  /**
   * Configuration related to {@link net.pumbas.quizapi.auth.providers.GoogleUserDataProvider}.
   */
  @Getter
  @Setter
  public static class Google {

    private String clientId;

  }

}
