package by.miaskor.domain.factory

import by.miaskor.domain.model.task.TaskResponse
import by.miaskor.domain.store.entity.TaskEntity
import org.springframework.stereotype.Component

@Component
class TaskDtoResponseFactory {

  fun makeTaskDtoResponse(taskEntity: TaskEntity): TaskResponse {
    return TaskResponse(
      taskName = taskEntity.taskName,
      taskState = taskEntity.taskState,
      date = taskEntity.date
    )
  }
}
