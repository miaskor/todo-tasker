package by.miaskor.domain.factory

import by.miaskor.domain.model.client.ClientResponse
import by.miaskor.domain.store.entity.ClientEntity
import org.springframework.stereotype.Component

@Component
class ClientDtoResponseFactory {

  fun makeClientDtoResponse(clientEntity: ClientEntity): ClientResponse {
    return ClientResponse(
      email = clientEntity.email,
      login = clientEntity.login,
    )
  }
}
