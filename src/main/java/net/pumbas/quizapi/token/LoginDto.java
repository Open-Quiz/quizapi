package net.pumbas.quizapi.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.pumbas.quizapi.user.UserDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

  UserDto user;
  TokenDto tokens;

}
