package com.miaskor.todo.spring.filter;

import by.miaskor.token.connector.connector.TokenConnector;
import by.miaskor.token.connector.domain.TokenDto;
import com.miaskor.todo.spring.service.SessionInvalidatorService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(value = 12)
@Component
public class TokenFilter extends HttpFilter {

  private final TokenConnector tokenConnector;
  private final SessionInvalidatorService sessionInvalidatorService;

  @Autowired
  public TokenFilter(TokenConnector tokenConnector,
      SessionInvalidatorService sessionInvalidatorService) {
    this.tokenConnector = tokenConnector;
    this.sessionInvalidatorService = sessionInvalidatorService;
  }

  @Override
  protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    String token = (String) req.getSession().getAttribute("token");
    if (token != null && !token.isEmpty()) {
      if (tokenConnector.validateToken(new TokenDto(token))) {
        req.getSession().setAttribute("validate", true);
        chain.doFilter(req, res);
      } else {
        sessionInvalidatorService.invalidate();
        res.sendRedirect("/auth");
      }
    } else {
      req.getSession().setAttribute("validate", false);
      chain.doFilter(req, res);
    }
  }
}
