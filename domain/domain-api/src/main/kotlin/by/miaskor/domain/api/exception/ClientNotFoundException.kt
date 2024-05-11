package by.miaskor.domain.api.exception

class ClientNotFoundException(vararg fieldToValue: Pair<String, Any?>, cause: Throwable? = null) : NotFoundException(
  message = "Client not found",
  fieldToValue = mapOf(*fieldToValue),
  cause = cause
)
