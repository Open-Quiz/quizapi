package net.pumbas.quizapi.middleware;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
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
    HandlerMethod method = this.getHandler(request);
    return method == null || !method.hasMethodAnnotation(this.annotationType);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) {
    HandlerMethod method = this.getHandler(request);
    if (method == null) {
      return; // Sanity check. This shouldn't happen due to the shouldNotFilter method
    }

    A annotation = method.getMethodAnnotation(this.annotationType);
    this.doFilter(request, response, filterChain, annotation);
  }

  protected abstract void doFilter(
      ServletRequest request, ServletResponse response,
      FilterChain filterChain, A annotation
  );


  private HandlerMethod getHandler(HttpServletRequest request) {
    try {
      HandlerExecutionChain chain = this.requestHandlers.getHandler(request);
      if (chain == null) {
        return null;
      }

      return (HandlerMethod) chain.getHandler();
    } catch (Exception e) {
      return null;
    }
  }

}
