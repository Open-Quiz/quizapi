package net.pumbas.quizapi.config;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
@EnableConfigurationProperties(Configuration.class)
@org.springframework.context.annotation.Configuration
public class ApplicationConfig {

  /**
   * Make {@link ZonedDateTime} compatible with auditing fields in JPA entities.
   *
   * @see <a href="https://stackoverflow.com/a/57757743">Stackoverflow Reference</a>
   */
  @Bean
  public DateTimeProvider auditingDateTimeProvider() {
    return () -> Optional.of(ZonedDateTime.now(ZoneOffset.UTC));
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        // TODO: Get allowed origins from config
        registry.addMapping("/**").allowedOrigins("http://localhost:5173").allowedMethods("*");
      }
    };
  }

}
