package net.pumbas.quizapi.user;

import net.pumbas.quizapi.user.providers.UserData;
import net.pumbas.quizapi.user.providers.UserDataProvider.Provider;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

  public UserDto userDtoFromUser(User user) {
    return UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .pictureUrl(user.getPictureUrl())
        .build();
  }

  public User userFromUserData(Provider provider, UserData userData) {
    return User.builder()
        .username(userData.getUsername())
        .pictureUrl(userData.getPictureUrl())
        .provider(provider)
        .providerId(userData.getProviderId())
        .build();
  }

}
