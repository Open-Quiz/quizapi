package net.pumbas.quizapi.user;

import net.pumbas.quizapi.config.Constants;
import net.pumbas.quizapi.exception.UnauthorizedException;
import net.pumbas.quizapi.user.LoginService.LoginResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_V1 + "/users")
public class UserController {

  private final LoginService loginService;

  public UserController(LoginService loginService) {
    this.loginService = loginService;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginDto> login(@RequestHeader("Authorization") String authorization) {
    String[] tokenParts = authorization.split(" ");

    if (tokenParts.length != 2) {
      throw new UnauthorizedException(
          "Invalid Authorization header. Expected '<provider> <token>' but got '%s'"
              .formatted(authorization));
    }

    String provider = tokenParts[0];
    String token = tokenParts[1];

    LoginResult loginResult = this.loginService.login(provider, token);
    return ResponseEntity.status(loginResult.isNewUser() ? HttpStatus.CREATED : HttpStatus.OK)
        .body(loginResult.getLoginDto());
  }

}
