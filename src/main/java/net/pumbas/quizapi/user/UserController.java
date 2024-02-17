package net.pumbas.quizapi.user;

import net.pumbas.quizapi.config.Constants;
import net.pumbas.quizapi.exception.UnauthorizedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_V1 + "/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/login")
  public LoginDto login(@RequestHeader("Authorization") String authorization) {
    String[] tokenParts = authorization.split(" ");

    if (tokenParts.length != 2) {
      throw new UnauthorizedException(
          "Invalid Authorization header. Expected '<provider> <token>' but got '%s'"
              .formatted(authorization));
    }

    String provider = tokenParts[0];
    String token = tokenParts[1];

    return this.userService.login(provider, token);
  }

}
