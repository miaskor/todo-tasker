package com.miaskor.todo.spring.service;

import java.util.Arrays;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public final class SessionInvalidatorService {

  private final HttpSession session;
  private final CookieInvalidator cookieInvalidator;

  public SessionInvalidatorService(HttpSession session,
      CookieInvalidator cookieInvalidator) {
    this.session = session;
    this.cookieInvalidator = cookieInvalidator;
  }

  public void invalidate() {
    cookieInvalidator.invalidateCookies(Arrays.asList(
        new Cookie("clientId", null),
        new Cookie("token", null)
    ));
    session.invalidate();
  }
}
