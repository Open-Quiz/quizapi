package net.pumbas.quizapi.user;

import net.pumbas.quizapi.config.Configuration;
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
      .build();

  private final Configuration configuration;
  private final UserRepository userRepository;

  @Autowired
  public UserService(Configuration configuration, UserRepository userRepository) {
    this.configuration = configuration;
    this.userRepository = userRepository;
  }


  @Override
  public void run(String... args) throws Exception {
    this.userRepository.save(TEST_USER);
    System.out.println(this.configuration.getGoogle().getClientId());
  }

}
