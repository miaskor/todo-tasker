package by.miaskor.domain.factories

import by.miaskor.domain.dto.ClientDtoResponse
import by.miaskor.domain.store.entities.ClientEntity
import org.springframework.stereotype.Component

@Component
class ClientDtoResponseFactory {

  fun makeClientDtoResponse(clientEntity: ClientEntity): ClientDtoResponse {
    return ClientDtoResponse(
      id = clientEntity.id,
      email = clientEntity.email,
      login = clientEntity.login,
    )
  }
}
