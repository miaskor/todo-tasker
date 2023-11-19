package com.miaskor.todo.spring.controller;

import by.miaskor.domain.connector.ClientConnector;
import by.miaskor.domain.model.client.CreateClientRequest;
import by.miaskor.token.connector.connector.TokenConnector;
import by.miaskor.token.connector.domain.ClientAuthDtoRequest;
import com.miaskor.todo.spring.service.AuthorizationService;
import java.net.URI;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthorizationController {

  private final ClientConnector clientConnector;
  private final TokenConnector tokenConnector;
  private final AuthorizationService authorizationService;

  @Autowired
  public AuthorizationController(ClientConnector clientConnector,
      TokenConnector tokenConnector, AuthorizationService authorizationService) {
    this.clientConnector = clientConnector;
    this.tokenConnector = tokenConnector;
    this.authorizationService = authorizationService;
  }

  @PostMapping("/auth")
  public ResponseEntity<Object> loginClient(@RequestBody CreateClientRequest createClientRequest) {
    Map<String, String> data = tokenConnector.createToken(
        new ClientAuthDtoRequest(createClientRequest.getLogin(), createClientRequest.getPassword())
    );
    authorizationService.auth(data);
    return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
        .location(URI.create("/todo")).build();
  }

  @PostMapping("/registration")
  public ResponseEntity<Object> registrationClient(@RequestBody CreateClientRequest createClientRequestDto) {
    clientConnector.createClient(createClientRequestDto);
    return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(URI.create("/auth")).build();
  }
}
