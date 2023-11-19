package by.miaskor.domain.model.task

import java.time.LocalDate

data class UpdateTaskRequest(
  val clientId: Long? = null,
  val taskState: TaskState? = null,
  val taskName: String? = null,
  val date: LocalDate? = null,
)
