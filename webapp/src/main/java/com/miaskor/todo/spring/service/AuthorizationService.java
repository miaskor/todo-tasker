package com.miaskor.todo.spring.service;

import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class AuthorizationService {

  private final HttpSession session;
  private final HttpServletResponse response;

  @Autowired
  public AuthorizationService(HttpSession session, HttpServletResponse response) {
    this.session = session;
    this.response = response;
  }

  public void auth(Map<String, String> data) {
    session.setAttribute("clientId", data.get("clientId"));
    session.setAttribute("token", data.get("token"));
    response.addCookie(new Cookie("clientId", data.get("clientId")));
  }
}
