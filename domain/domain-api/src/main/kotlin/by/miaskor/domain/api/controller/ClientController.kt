package by.miaskor.domain.api.controller

import by.miaskor.domain.factory.ClientDtoResponseFactory
import by.miaskor.domain.model.client.ClientRequest
import by.miaskor.domain.model.client.ClientResponse
import by.miaskor.domain.model.client.CreateClientRequest
import by.miaskor.domain.service.ClientService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/clients")
open class ClientController(
  private val clientService: ClientService,
  private val clientDtoResponseFactory: ClientDtoResponseFactory,
) {

  @PostMapping
  @ResponseStatus(CREATED)
  fun create(
    @RequestBody
    @Validated createClientRequest: CreateClientRequest,
  ): ClientResponse {
    return clientService.create(createClientRequest)
      .let(clientDtoResponseFactory::makeClientDtoResponse)
  }

  @GetMapping("/{id}")
  fun get(@PathVariable id: Long): ClientResponse {
    return clientService.getById(id)
      .let(clientDtoResponseFactory::makeClientDtoResponse)
  }

  @PostMapping("/search")
  fun search(@RequestBody clientRequest: ClientRequest): ClientResponse {
    return clientService.search(clientRequest)
      .let(clientDtoResponseFactory::makeClientDtoResponse)
  }

  @PatchMapping("/{id}")
  fun patch(
    @PathVariable id: Long,
    @RequestBody clientRequest: ClientRequest,
  ): ClientResponse {
    return clientService.updateById(id, clientRequest)
      .let(clientDtoResponseFactory::makeClientDtoResponse)
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(NO_CONTENT)
  fun delete(@PathVariable id: Long) {
    clientService.delete(id)
  }
}
