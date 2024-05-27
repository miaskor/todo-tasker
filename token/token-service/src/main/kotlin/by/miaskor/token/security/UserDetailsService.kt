package by.miaskor.token.security

import by.miaskor.domain.connector.ClientConnector
import by.miaskor.domain.model.client.ClientRequest
import by.miaskor.token.exception.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.*

class UserDetailsService(
  private val clientConnector: ClientConnector,
  private val userFactory: UserFactory,
) : UserDetailsService {

  override fun loadUserByUsername(login: String): UserDetails {
    val clientRequest = ClientRequest(login = login)
    val result = clientConnector.getBy(clientRequest)

    return Optional.ofNullable(result.data)
      .map(userFactory::create)
      .orElseThrow { AuthenticationException("Client response doesn't have data") }
  }
}
