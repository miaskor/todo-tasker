package com.miaskor.todo.spring.filter;

import static com.miaskor.todo.spring.util.URLs.LOGIN;
import static com.miaskor.todo.spring.util.URLs.getAllPublicURL;

import java.io.IOException;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(value = 20)
@Component
public class AccessFilter extends HttpFilter {

  private static final Set<String> PUBLIC_PAGES = getAllPublicURL();

  @Override
  protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    String requestURI = req.getRequestURI();
    if (requestURI.contains("about")) {
      req.getSession().setAttribute("about", true);
    } else {
      req.getSession().removeAttribute("about");
    }

    if (PUBLIC_PAGES.stream().anyMatch(requestURI::startsWith)) {
      chain.doFilter(req, res);
    } else if (Boolean.TRUE.equals(req.getSession().getAttribute("validate"))) {
      chain.doFilter(req, res);
    } else {
      res.sendRedirect(req.getHeader("referer") == null
          ? LOGIN : req.getHeader("referer"));
    }
  }
}
