package net.pumbas.quizapi.user;

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

  private final UserRepository userRepository;
  private final UserDataProviderFactory userDataProviderFactory;

  @Autowired
  public UserService(
      UserRepository userRepository,
      UserDataProviderFactory userDataProviderFactory
  ) {
    this.userRepository = userRepository;
    this.userDataProviderFactory = userDataProviderFactory;
  }

  @Override
  public void run(String... args) throws Exception {
    this.userRepository.save(TEST_USER);
  }

  public LoginDto login(String provider, String token) {
    UserDataProvider userDataProvider = this.userDataProviderFactory.getProvider(provider);

    return null;
  }

}
