package com.miaskor.todo.spring.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(value = 10)
@Component
public class EncodingFilter extends HttpFilter {

  @Override
  protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    req.setCharacterEncoding(StandardCharsets.UTF_8.name());
    res.setCharacterEncoding(StandardCharsets.UTF_8.name());
    chain.doFilter(req, res);
  }
}
