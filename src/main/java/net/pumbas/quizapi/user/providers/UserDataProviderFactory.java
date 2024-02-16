package net.pumbas.quizapi.user.providers;

import net.pumbas.quizapi.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

@Service
public class UserDataProviderFactory {

  private final GoogleUserDataProvider googleUserDataProvider;

  public UserDataProviderFactory(GoogleUserDataProvider googleUserDataProvider) {
    this.googleUserDataProvider = googleUserDataProvider;
  }

  /**
   * Factory method for getting the user data provider based on the case-insensitive provider name.
   * If the provider is not supported, an {@link UnauthorizedException} is thrown.
   *
   * @param provider The case-insensitive name of the provider.
   * @return The {@link UserDataProvider} instance
   */
  public UserDataProvider getProvider(String provider) {
    return switch (provider.toLowerCase()) {
      case "google" -> this.googleUserDataProvider;
      default -> throw new UnauthorizedException(
          "The user data provider is not supported: '%s'".formatted(provider));
    };
  }

}
