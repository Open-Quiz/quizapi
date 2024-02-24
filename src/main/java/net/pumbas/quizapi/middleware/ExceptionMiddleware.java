package net.pumbas.quizapi.middleware;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

/**
 * Handles the exceptions thrown by any filters.
 */
@Order(0)
@Component
public class ExceptionMiddleware extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain
  ) throws IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (ResponseStatusException e) {
      response.setStatus(e.getStatusCode().value());
      String reason = e.getReason();
      if (reason != null) {
        response.getWriter().write(reason);
      }
    } catch (Exception e) {
      this.logger.error("Unexpected exception in %s %s: %s"
          .formatted(request.getMethod(), request.getRequestURI(), e));
      e.printStackTrace();

      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.getWriter().write("There was an unexpected error while processing your request");
    }
  }

}
