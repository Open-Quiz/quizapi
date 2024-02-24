package net.pumbas.quizapi.token;

import java.util.Optional;
import lombok.Data;
import net.pumbas.quizapi.user.User;
import net.pumbas.quizapi.user.UserDto;
import net.pumbas.quizapi.user.UserService;
import net.pumbas.quizapi.user.providers.UserData;
import net.pumbas.quizapi.user.providers.UserDataProvider;
import net.pumbas.quizapi.user.providers.UserDataProviderFactory;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  private final UserService userService;
  private final TokenService tokenService;
  private final UserDataProviderFactory userDataProviderFactory;

  public LoginService(
      UserService userService,
      TokenService tokenService,
      UserDataProviderFactory userDataProviderFactory
  ) {
    this.userService = userService;
    this.tokenService = tokenService;
    this.userDataProviderFactory = userDataProviderFactory;
  }

  public LoginResult login(String provider, String token) {
    UserDataProvider userDataProvider = this.userDataProviderFactory.getProvider(provider);
    UserData userData = userDataProvider.extractUserData(token);

    Optional<User> oUser = this.userService.getUserByProvider(
        userDataProvider.getName(), userData.getProviderId());

    boolean isNewUser = oUser.isEmpty();
    UserDto user = oUser.map(existingUser -> this.userService.refreshUser(existingUser, userData))
        .orElseGet(() -> this.userService.createUser(userDataProvider.getName(), userData));

    LoginDto loginDto = LoginDto.builder()
        .user(user)
        .tokens(TokenDto.builder()
            .accessToken(this.tokenService.generateAccessToken(user.getId()))
            .refreshToken(this.tokenService.generateNewRefreshToken(user.getId()))
            .build())
        .build();

    return new LoginResult(isNewUser, loginDto);
  }

  @Data
  public static class LoginResult {

    private final boolean isNewUser;
    private final LoginDto loginDto;

  }

}
