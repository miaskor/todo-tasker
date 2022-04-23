package by.miaskor.domain.dto

data class ClientDtoRequest(
  val email: String = "",
  val login: String = "",
  val password: String? = null,
  val botId: Long = 0
)
