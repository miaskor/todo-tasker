package by.miaskor.common.exception.handler.model

data class Result<T>(
  val data: T? = null,
  val error: ErrorResult? = null,
)

data class ErrorResult(
  val code: Int,
  val detail: String,
)