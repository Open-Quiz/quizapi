package net.pumbas.quizapi;

import net.pumbas.quizapi.config.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableJpaAuditing
@EnableConfigurationProperties(Configuration.class)
@SpringBootApplication
public class QuizApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(QuizApiApplication.class, args);
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
