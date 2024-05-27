package by.miaskor.domain.model.client

import by.miaskor.domain.model.client.ClientType.USER
import org.springframework.security.core.GrantedAuthority

data class ClientResponse(
  val login: String = "",
  val email: String = "",
  val type: ClientType = USER,
  val blocked: Boolean = true,
  val password: String = ""
)

enum class ClientType:GrantedAuthority {
  ADMIN,
  USER;

  override fun getAuthority() = name
}
