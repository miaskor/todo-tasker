package by.miaskor.domain.api.exception

private fun parseFields(fieldToValue: Map<String, Any?>): String {
  return fieldToValue.entries
    .joinToString { (field, value) -> "$field=$value" }
}

open class NotFoundException(fieldToValue: Map<String, Any?>, message: String) : RuntimeException
  ("$message, ${parseFields(fieldToValue)}")