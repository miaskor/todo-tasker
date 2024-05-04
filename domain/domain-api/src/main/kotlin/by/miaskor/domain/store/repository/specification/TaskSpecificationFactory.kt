package by.miaskor.domain.store.repository.specification

import by.miaskor.domain.model.task.SearchTaskRequest
import by.miaskor.domain.model.task.TaskState
import by.miaskor.domain.store.entity.ClientEntity
import by.miaskor.domain.store.entity.TaskEntity
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Join
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

@Component
class TaskSpecificationFactory {

  /**
   * We cannot join two entities using a basic type like Long,
   * so we need to establish a ManyToOne relationship by including ClientEntity in TaskEntity.
   * I added a fetch here to prevent a second query for retrieving ClientEntity.
   * The query can be optimized because only the necessary columns from ClientEntity are included.
   */
  fun createFindAllBy(searchTaskRequest: SearchTaskRequest): Specification<TaskEntity> {
    val predicates = mutableListOf<Predicate>()
    return Specification { root, _, cb ->
      if (searchTaskRequest.botId == null) {
        predicates.apply {
          setTaskEntityFields(searchTaskRequest, cb, root)
        }
      } else {
        val join: Join<TaskEntity, ClientEntity> = root.join("client")
        root.fetch<TaskEntity, ClientEntity>("client")

        predicates.apply {
          add(cb.equal(join.get<Long>(ClientEntity::botId.name), searchTaskRequest.botId))
          setTaskEntityFields(searchTaskRequest, cb, root)
        }
      }
      cb.and(*predicates.toTypedArray())
    }
  }

  private fun MutableList<Predicate>.setTaskEntityFields(
    searchTaskRequest: SearchTaskRequest,
    cb: CriteriaBuilder,
    root: Root<TaskEntity>,
  ) {
    if (searchTaskRequest.clientId != null) {
      add(
        cb.equal(
          root.get<ClientEntity>(TaskEntity::client.name).get<Long>(ClientEntity::id.name), searchTaskRequest.clientId
        )
      )
    }
    if (searchTaskRequest.taskState != null) {
      add(cb.equal(root.get<TaskState>(TaskEntity::taskState.name), searchTaskRequest.taskState))
    }
    if (searchTaskRequest.dateFrom != null) {
      add(cb.greaterThanOrEqualTo(root.get(TaskEntity::date.name), searchTaskRequest.dateFrom))
    }
    if (searchTaskRequest.dateTo != null) {
      add(cb.lessThanOrEqualTo(root.get(TaskEntity::date.name), searchTaskRequest.dateTo))
    }
  }
}