package com.miaskor.todo.spring.controller;

import by.miaskor.domain.connector.ClientConnector;
import by.miaskor.domain.dto.ClientDtoRequest;
import by.miaskor.domain.dto.ClientDtoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "clientController")
@RequestMapping("/clients")
public class ClientController {

  private final ClientConnector clientConnector;

  @Autowired
  public ClientController(ClientConnector clientConnector) {
    this.clientConnector = clientConnector;
  }

  @PatchMapping("/update/{id}")
  public ClientDtoResponse update(
      @PathVariable("id") Integer clientId,
      @RequestBody ClientDtoRequest clientDtoRequest
  ) {
    return clientConnector.update(clientId, clientDtoRequest);
  }

  @GetMapping("/{id}")
  public ClientDtoResponse getById(@PathVariable("id") Integer clientId) {
    return clientConnector.getById(clientId);
  }
}
