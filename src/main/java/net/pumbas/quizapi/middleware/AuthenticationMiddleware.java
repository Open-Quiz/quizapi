package net.pumbas.quizapi.middleware;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import net.pumbas.quizapi.exception.UnauthorizedException;
import net.pumbas.quizapi.middleware.annotations.Secured;
import net.pumbas.quizapi.token.TokenService;
import net.pumbas.quizapi.user.User;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class AuthenticationMiddleware extends AnnotatedFilter<Secured> {

  private final TokenService tokenService;

  public AuthenticationMiddleware(
      TokenService tokenService,
      RequestMappingHandlerMapping requestHandlers
  ) {
    super(requestHandlers, Secured.class);
    this.tokenService = tokenService;
  }

  @Override
  protected void doFilter(
      HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain, Secured annotation
  ) throws ServletException, IOException {
    this.logger.info(
        "Authorizing request: %s %s".formatted(request.getMethod(), request.getRequestURI()));

    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || !header.startsWith("Bearer ")) {
      throw new UnauthorizedException("No bearer token provided in the Authorization header");
    }

    String token = header.substring("Bearer ".length());
    User user = this.tokenService.validateAccessToken(token);

    AuthContext.setCurrentUser(user);
    filterChain.doFilter(request, response);
  }

  /**
   * This context stores information about the current authenticated user. It uses a
   * {@link ThreadLocal} which ensures that each thread can have a different authenticated user.
   */
  public static class AuthContext {

    private static final ThreadLocal<User> CURRENT_USER = new ThreadLocal<>();

    public static User getCurrentUser() {
      return CURRENT_USER.get();
    }

    protected static void setCurrentUser(User user) {
      CURRENT_USER.set(user);
    }

  }

}
