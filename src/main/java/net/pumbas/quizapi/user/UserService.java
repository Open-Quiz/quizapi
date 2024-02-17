package net.pumbas.quizapi.user;

import java.util.Optional;
import net.pumbas.quizapi.user.providers.UserData;
import net.pumbas.quizapi.user.providers.UserDataProvider;
import net.pumbas.quizapi.user.providers.UserDataProvider.Provider;
import net.pumbas.quizapi.user.providers.UserDataProviderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class UserService implements CommandLineRunner {

  public static final User TEST_USER = User.builder()
      .id(1L)
      .username("pumbas600")
      .pictureUrl(
          "https://lh3.googleusercontent.com/ogw/ANLem4a3Y854mZAT_2a0x-CH_9gQ3qsvEFEeHKFAVQTkOA=s32-c-mo")
      .provider(Provider.GOOGLE)
      .providerId("108296527190784247583")
      .build();

  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final UserDataProviderFactory userDataProviderFactory;

  @Autowired
  public UserService(
      UserMapper userMapper,
      UserRepository userRepository,
      UserDataProviderFactory userDataProviderFactory
  ) {
    this.userMapper = userMapper;
    this.userRepository = userRepository;
    this.userDataProviderFactory = userDataProviderFactory;
  }

  @Override
  public void run(String... args) throws Exception {
    this.userRepository.save(TEST_USER);
  }

  public LoginDto login(String provider, String token) {
    UserDataProvider userDataProvider = this.userDataProviderFactory.getProvider(provider);
    UserData userData = userDataProvider.extractUserData(token);

    Optional<User> oUser = this.userRepository.getUserByProvider(
        userDataProvider.getName(), userData.getProviderId());

    User user = oUser.map(existingUser -> this.refreshUser(existingUser, userData))
        .orElseGet(() -> this.createUser(userDataProvider.getName(), userData));

    return null;
  }


  public User refreshUser(User user, UserData userData) {
    user.setUsername(userData.getUsername());
    user.setPictureUrl(userData.getPictureUrl());

    return this.userRepository.save(user);
  }

  public User createUser(Provider provider, UserData userData) {
    User user = this.userMapper.userFromUserData(provider, userData);
    return this.userRepository.save(user);
  }

}
