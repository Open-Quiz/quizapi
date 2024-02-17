package net.pumbas.quizapi.user;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

  UserDto user;
  String accessToken;
  String refreshToken;
  ZonedDateTime accessTokenExpiresAt;
  ZonedDateTime refreshTokenExpiresAt;

}
