package net.pumbas.quizapi.user;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

  UserDto userDto;
  String accessToken;
  String refreshToken;
  LocalDateTime accessTokenExpiresAt;
  LocalDateTime refreshTokenExpiresAt;

}
