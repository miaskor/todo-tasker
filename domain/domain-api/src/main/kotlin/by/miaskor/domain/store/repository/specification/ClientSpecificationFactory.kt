package by.miaskor.domain.store.repository.specification

import by.miaskor.domain.model.client.ClientRequest
import by.miaskor.domain.store.entity.ClientEntity
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import javax.persistence.criteria.Predicate

@Component
class ClientSpecificationFactory {

  fun createFindAllBy(clientRequest: ClientRequest): Specification<ClientEntity> {
    return Specification { root, _, cb ->
      val predicates = mutableListOf<Predicate>().apply {
        if (clientRequest.login != null) {
          add(cb.equal(root.get<String>("login"), clientRequest.login))
        }
        if (clientRequest.email != null) {
          add(cb.equal(root.get<String>("email"), clientRequest.email))
        }
        if (clientRequest.password != null) {
          add(cb.equal(root.get<String>("password"), clientRequest.password))
        }
        if (clientRequest.botId != null) {
          add(cb.equal(root.get<Long>("botId"), clientRequest.botId))
        }
      }
      cb.and(*predicates.toTypedArray())
    }
  }
}