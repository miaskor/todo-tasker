package by.miaskor.domain.dto

data class TaskDtoResponse(
  val id: Int = 0,
  val taskName: String = "",
  val taskState: TaskState = TaskState.UPCOMING
)
