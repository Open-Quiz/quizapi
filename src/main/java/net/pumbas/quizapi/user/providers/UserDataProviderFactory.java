package net.pumbas.quizapi.user.providers;

import java.util.HashMap;
import java.util.Map;
import net.pumbas.quizapi.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

@Service
public class UserDataProviderFactory {

  private final Map<String, UserDataProvider> providerMap = new HashMap<>();

  public UserDataProviderFactory(GoogleUserDataProvider googleUserDataProvider) {
    this.providerMap.put(googleUserDataProvider.getName().toString(), googleUserDataProvider);
  }

  /**
   * Factory method for getting the user data provider based on the case-insensitive provider name.
   * If the provider is not supported, an {@link UnauthorizedException} is thrown.
   *
   * @param providerName The case-insensitive name of the provider.
   * @return The {@link UserDataProvider} instance
   */
  public UserDataProvider getProvider(String providerName) {
    UserDataProvider userDataProvider = this.providerMap.get(providerName.toUpperCase());
    if (userDataProvider == null) {
      throw new UnauthorizedException(
          "The user data provider is not supported: '%s'".formatted(providerName));
    }

    return userDataProvider;
  }

}
