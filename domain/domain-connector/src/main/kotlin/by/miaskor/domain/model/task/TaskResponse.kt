package by.miaskor.domain.model.task

import java.time.LocalDate

data class TaskResponse(
  val taskName: String,
  val taskState: TaskState,
  val date: LocalDate,
)
