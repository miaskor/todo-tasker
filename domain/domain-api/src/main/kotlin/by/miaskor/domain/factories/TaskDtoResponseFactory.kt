package by.miaskor.domain.factories

import by.miaskor.domain.dto.TaskDtoResponse
import by.miaskor.domain.store.entities.TaskEntity
import org.springframework.stereotype.Component

@Component
class TaskDtoResponseFactory {

  fun makeTaskDtoResponse(taskEntity: TaskEntity): TaskDtoResponse {
    return TaskDtoResponse(
      id = taskEntity.id,
      taskName = taskEntity.taskName,
      taskState = taskEntity.taskState
    )
  }
}
