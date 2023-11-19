package com.miaskor.todo.spring.controller;

import by.miaskor.domain.connector.ClientConnector;
import by.miaskor.domain.model.client.ClientResponse;
import by.miaskor.domain.model.client.CreateClientRequest;
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
  public ClientResponse update(
      @PathVariable("id") Integer clientId,
      @RequestBody CreateClientRequest createClientRequest
  ) {
    return clientConnector.update(clientId, createClientRequest);
  }

  @GetMapping("/{id}")
  public ClientResponse getById(@PathVariable("id") Integer clientId) {
    return clientConnector.getById(clientId);
  }
}
