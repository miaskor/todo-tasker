package by.miaskor.domain.api.exception

class TaskNotFoundException(vararg fieldToValue: Pair<String, Any?>) : NotFoundException(
  message = "Task not found",
  fieldToValue = mapOf(*fieldToValue)
)
