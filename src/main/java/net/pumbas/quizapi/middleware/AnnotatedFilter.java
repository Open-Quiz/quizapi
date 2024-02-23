package net.pumbas.quizapi.middleware;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RequiredArgsConstructor
public abstract class AnnotatedFilter<A extends Annotation> extends OncePerRequestFilter {

  private final RequestMappingHandlerMapping requestHandlers;

  private final Class<A> annotationType;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    A annotation = this.getAnnotation(request);
    return annotation == null;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) {
    A annotation = this.getAnnotation(request);
    if (annotation == null) {
      this.logger.warn(
          "Annotation not found for AnnotatedFilter (%s) but doFilterInternal still called: %s %s"
              .formatted(
                  this.getClass().getCanonicalName(),
                  request.getMethod(),
                  request.getRequestURI()));
      return; // Sanity check. This shouldn't happen due to the shouldNotFilter method
    }

    this.doFilter(request, response, filterChain, annotation);
  }

  protected abstract void doFilter(
      ServletRequest request, ServletResponse response,
      FilterChain filterChain, A annotation
  );


  /**
   * Attempt to get the annotation from the handler of the request. This will first check the method
   * and, if not found it will then check the class.
   *
   * @param request The request to get the handler's annotation from
   * @return The annotation if found, otherwise {@code null}
   */
  private A getAnnotation(HttpServletRequest request) {
    try {
      HandlerExecutionChain chain = this.requestHandlers.getHandler(request);
      if (chain == null) {
        return null;
      }

      HandlerMethod handler = (HandlerMethod) chain.getHandler();
      if (handler.hasMethodAnnotation(this.annotationType)) {
        return handler.getMethodAnnotation(this.annotationType);
      }

      Method method = handler.getMethod();
      Class<?> declaringClass = method.getDeclaringClass();

      if (declaringClass.isAnnotationPresent(this.annotationType)) {
        return declaringClass.getAnnotation(this.annotationType);
      }

      return null;
    } catch (Exception e) {
      return null;
    }
  }

}
