package by.miaskor.domain.model.client

data class ClientRequest(
  val login: String? = null,
  val email: String? = null,
  val password: String? = null,
  val botId: Long? = null,
) {
  fun isEmpty(): Boolean {
    return login == null && email == null &&
        password == null && botId == null
  }

  override fun toString(): String {
    return "ClientRequest(login=$login, email=$email, botId=$botId)"
  }
}
