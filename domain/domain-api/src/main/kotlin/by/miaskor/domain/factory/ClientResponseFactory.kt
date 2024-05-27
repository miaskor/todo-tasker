package by.miaskor.domain.factory

import by.miaskor.domain.model.client.ClientResponse
import by.miaskor.domain.store.entity.ClientEntity
import org.springframework.stereotype.Component

@Component
class ClientResponseFactory {

  fun makeClientResponse(clientEntity: ClientEntity): ClientResponse {
    return ClientResponse(
      email = clientEntity.email,
      login = clientEntity.login,
      type = clientEntity.type,
      blocked = clientEntity.blocked,
      password = clientEntity.password
    )
  }
}
