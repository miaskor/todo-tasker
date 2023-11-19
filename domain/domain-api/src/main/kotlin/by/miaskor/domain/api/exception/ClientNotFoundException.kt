package by.miaskor.domain.api.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ClientNotFoundException(vararg fieldToValue: Pair<String, Any?>) : NotFoundException(
  message = "Client not found",
  fieldToValue = mapOf(*fieldToValue)
)