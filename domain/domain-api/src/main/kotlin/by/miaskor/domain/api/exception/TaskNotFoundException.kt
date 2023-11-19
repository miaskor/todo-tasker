package by.miaskor.domain.api.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class TaskNotFoundException(vararg fieldToValue: Pair<String, Any?>) : NotFoundException(
  message = "Task not found",
  fieldToValue = mapOf(*fieldToValue)
)
