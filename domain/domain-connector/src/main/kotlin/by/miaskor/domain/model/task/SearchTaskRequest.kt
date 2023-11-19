package by.miaskor.domain.model.task

import java.time.LocalDate

data class SearchTaskRequest(
  val botId: Long? = null,
  val clientId: Long? = null,
  val taskState: TaskState? = null,
  val dateFrom: LocalDate? = null,
  val dateTo: LocalDate? = null,
) {
  fun isValid(): Boolean {
    return botId != null || clientId != null
  }
}
