package com.miaskor.todo.spring.filter;

import com.miaskor.todo.spring.service.SessionInvalidatorService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(11)
@Component
public class LogoutFilter extends HttpFilter {

  private final SessionInvalidatorService sessionInvalidatorService;

  public LogoutFilter(SessionInvalidatorService sessionInvalidatorService) {
    this.sessionInvalidatorService = sessionInvalidatorService;
  }

  @Override
  protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (request.getRequestURI().contains("/logout")) {
      sessionInvalidatorService.invalidate();
      response.sendRedirect("/auth");
    } else {
      chain.doFilter(request, response);
    }
  }
}
