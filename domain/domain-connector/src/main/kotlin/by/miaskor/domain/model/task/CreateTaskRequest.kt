package by.miaskor.domain.model.task

import java.time.LocalDate

data class CreateTaskRequest(
  val clientId: Long = -1,
  val date: LocalDate = LocalDate.now(),
  val taskName: String = "",
  val taskState: TaskState = TaskState.UPCOMING,
)
