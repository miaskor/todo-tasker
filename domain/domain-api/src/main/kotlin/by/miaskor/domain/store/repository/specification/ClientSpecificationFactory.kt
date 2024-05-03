package by.miaskor.domain.store.repository.specification

import by.miaskor.domain.model.client.ClientRequest
import by.miaskor.domain.store.entity.ClientEntity
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.lang.reflect.Field
import javax.persistence.criteria.Predicate

@Component
class ClientSpecificationFactory {

  fun createFindAllBy(clientRequest: ClientRequest): Specification<ClientEntity> {
    return Specification { root, _, cb ->
      val predicates = mutableListOf<Predicate>().apply {
        clientRequest.javaClass.declaredFields.forEach { field: Field? ->
          field?.trySetAccessible()
          val fieldValue = field?.get(clientRequest)
          if (fieldValue != null) {
            add(cb.equal(root.get<String>(field.name), fieldValue))
          }
        }
      }
      cb.and(*predicates.toTypedArray())
    }
  }
}