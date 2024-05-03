package by.miaskor.token.security

import by.miaskor.domain.connector.ClientConnector
import by.miaskor.domain.model.client.ClientRequest
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class UserDetailsService(
  private val clientConnector: ClientConnector,
  private val userFactory: UserFactory
) : UserDetailsService {

  override fun loadUserByUsername(login: String): UserDetails {
    val client = clientConnector.getBy(ClientRequest(login = login))
    return userFactory.create(client)
  }
}
