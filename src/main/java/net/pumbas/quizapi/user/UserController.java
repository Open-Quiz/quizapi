package net.pumbas.quizapi.user;

import net.pumbas.quizapi.config.Constants;
import net.pumbas.quizapi.middleware.AuthenticationMiddleware.AuthContext;
import net.pumbas.quizapi.middleware.annotations.Secured;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_V1 + "/users")
public class UserController {

  private final UserMapper userMapper;

  public UserController(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Secured
  @GetMapping("/@me")
  public ResponseEntity<UserDto> getMe() {
    User requester = AuthContext.getCurrentUser();
    return ResponseEntity.ok(this.userMapper.userDtoFromUser(requester));
  }

}
