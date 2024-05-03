package by.miaskor.domain.service

import by.miaskor.domain.api.exception.BadRequestException
import by.miaskor.domain.api.exception.TaskNotFoundException
import by.miaskor.domain.model.task.CreateTaskRequest
import by.miaskor.domain.model.task.SearchTaskRequest
import by.miaskor.domain.model.task.UpdateTaskRequest
import by.miaskor.domain.store.entity.TaskEntity
import by.miaskor.domain.store.repository.TaskRepository
import by.miaskor.domain.store.repository.specification.TaskSpecificationFactory
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
open class TaskService(
  private val taskRepository: TaskRepository,
  private val taskSpecificationFactory: TaskSpecificationFactory,
  private val clientService: ClientService,
) {

  @Transactional(readOnly = true)
  open fun search(searchTaskRequest: SearchTaskRequest): Sequence<TaskEntity> {
    if (!searchTaskRequest.isValid()) throw BadRequestException("Must be botId or clientId")

    return taskSpecificationFactory.createFindAllBy(searchTaskRequest)
      .let(taskRepository::findAll)
      .asSequence()
  }

  open fun create(createTaskRequest: CreateTaskRequest): TaskEntity {
    return clientService.getById(createTaskRequest.clientId)
      .let { clientEntity ->
        TaskEntity(
          clientId = clientEntity.id,
          taskName = createTaskRequest.taskName,
          date = createTaskRequest.date,
          taskState = createTaskRequest.taskState
        )
      }
      .also(taskRepository::saveAndFlush)
  }

  open fun updateById(id: Long, updateTaskRequest: UpdateTaskRequest): TaskEntity {
    val taskEntity = taskRepository.findById(id)
      .orElseThrow { TaskNotFoundException("id" to id) }

    Optional.ofNullable(updateTaskRequest.clientId)
      .map(clientService::getById)
      .get()

    return TaskEntity(
      id = taskEntity.id,
      clientId = updateTaskRequest.clientId ?: taskEntity.clientId,
      taskName = updateTaskRequest.taskName ?: taskEntity.taskName,
      date = updateTaskRequest.date ?: taskEntity.date,
      taskState = updateTaskRequest.taskState ?: taskEntity.taskState
    )
      .also(taskRepository::saveAndFlush)
  }

  open fun deleteById(id: Long) {
    taskRepository.findById(id)
      .orElseThrow { TaskNotFoundException("id" to id) }

    taskRepository.deleteById(id)
  }

  open fun getAllByClientIdWithPagination(clientId: Long, pageable: Pageable): Sequence<TaskEntity> {
    return taskRepository.findAllByClientId(clientId, pageable)
      .asSequence()
  }
}