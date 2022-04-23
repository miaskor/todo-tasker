package by.miaskor.domain.dto

import java.time.LocalDate

data class TaskDtoRequest(
  val clientId: Int = 0,
  val date: LocalDate = LocalDate.now(),
  val taskName: String = "",
  val taskState: TaskState = TaskState.UPCOMING
)
