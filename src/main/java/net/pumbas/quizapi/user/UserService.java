package net.pumbas.quizapi.user;

import java.util.Optional;
import net.pumbas.quizapi.exception.NotFoundException;
import net.pumbas.quizapi.user.providers.UserData;
import net.pumbas.quizapi.user.providers.UserDataProvider.Provider;
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

  @Autowired
  public UserService(UserMapper userMapper, UserRepository userRepository) {
    this.userMapper = userMapper;
    this.userRepository = userRepository;
  }

  @Override
  public void run(String... args) {
    this.userRepository.save(TEST_USER);
  }

  public UserDto refreshUser(User user, UserData userData) {
    user.setUsername(userData.getUsername());
    user.setPictureUrl(userData.getPictureUrl());

    User updatedUser = this.userRepository.save(user);
    return this.userMapper.userDtoFromUser(updatedUser);
  }

  public UserDto createUser(Provider provider, UserData userData) {
    User user = this.userMapper.userFromUserData(provider, userData);
    User createdUser = this.userRepository.save(user);

    return this.userMapper.userDtoFromUser(createdUser);
  }

  public Optional<User> getUserByProvider(Provider provider, String providerId) {
    return this.userRepository.getUserByProvider(provider, providerId);
  }

  public User getUser(Long userId) {
    return this.userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("Could not find user with id: " + userId));
  }

  public User getUserReference(Long userId) {
    return this.userRepository.getReferenceById(userId);
  }

}
