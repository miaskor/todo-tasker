package com.miaskor.todo.spring.service;

import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class CookieInvalidator {

  private final HttpServletResponse response;

  @Autowired
  public CookieInvalidator(HttpServletResponse response) {
    this.response = response;
  }

  public void invalidateCookies(List<Cookie> cookies) {
    cookies.forEach(
        cookie -> {
          cookie.setMaxAge(0);
          cookie.setValue(null);
          response.addCookie(cookie);
        }
    );
  }
}
