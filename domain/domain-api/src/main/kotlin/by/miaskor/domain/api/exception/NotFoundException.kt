package by.miaskor.domain.api.exception

import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus

private fun parseFields(fieldToValue: Map<String, Any?>): String {
  return fieldToValue.entries
    .joinToString { (field, value) -> "$field=$value" }
}

@ResponseStatus(NOT_FOUND)
open class NotFoundException(fieldToValue: Map<String, Any?>, message: String, cause: Throwable? = null) :
  RuntimeException("$message, ${parseFields(fieldToValue)}", cause)