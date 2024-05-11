package by.miaskor.domain.api.exception

class TaskNotFoundException(vararg fieldToValue: Pair<String, Any?>, cause: Throwable? = null) : NotFoundException(
  message = "Task not found",
  fieldToValue = mapOf(*fieldToValue),
  cause = cause
)
