package net.pumbas.quizapi.user;

import org.springframework.stereotype.Service;

@Service
public class UserMapper {
  public UserDto userDtoFromUser(User user) {
    return UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .profilePictureUrl(user.getProfilePictureUrl())
        .build();
  }
}
