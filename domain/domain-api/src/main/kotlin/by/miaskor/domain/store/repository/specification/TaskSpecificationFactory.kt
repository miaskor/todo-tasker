package by.miaskor.domain.store.repository.specification

import by.miaskor.domain.model.task.SearchTaskRequest
import by.miaskor.domain.model.task.TaskState
import by.miaskor.domain.store.entity.ClientEntity
import by.miaskor.domain.store.entity.TaskEntity
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import javax.persistence.criteria.Join
import javax.persistence.criteria.Predicate

@Component
class TaskSpecificationFactory {

  /**
   * We cannot join two entities using a basic type like Long,
   * so we need to establish a ManyToOne relationship by including ClientEntity in TaskEntity.
   * I added a fetch here to prevent a second query for retrieving ClientEntity.
   * The query can be optimized because only the necessary columns from ClientEntity are included.
   */
  fun createFindAllBy(searchTaskRequest: SearchTaskRequest): Specification<TaskEntity> {
    return Specification { root, _, cb ->
      val join: Join<TaskEntity, ClientEntity> = root.join("client")

      root.fetch<TaskEntity, ClientEntity>("client")

      val predicates = mutableListOf<Predicate>().apply {
        if (searchTaskRequest.botId != null) {
          add(cb.equal(join.get<Long>("botId"), searchTaskRequest.botId))
        }
        if (searchTaskRequest.clientId != null) {
          add(cb.equal(join.get<Long>("id"), searchTaskRequest.clientId))
        }
        if (searchTaskRequest.taskState != null) {
          add(cb.equal(root.get<TaskState>("taskState"), searchTaskRequest.taskState))
        }
        if (searchTaskRequest.dateFrom != null) {
          add(cb.greaterThanOrEqualTo(root.get("date"), searchTaskRequest.dateFrom))
        }
        if (searchTaskRequest.dateTo != null) {
          add(cb.lessThanOrEqualTo(root.get("date"), searchTaskRequest.dateTo))
        }
      }

      cb.and(*predicates.toTypedArray())
    }
  }
}