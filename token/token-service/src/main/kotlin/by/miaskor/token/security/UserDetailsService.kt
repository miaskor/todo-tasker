package by.miaskor.token.security

import by.miaskor.domain.connector.ClientConnector
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class UserDetailsService(
  private val clientConnector: ClientConnector,
  private val userFactory: UserFactory
) : UserDetailsService {

  override fun loadUserByUsername(login: String): UserDetails {
    val client = clientConnector.getByLogin(login)
    return userFactory.create(client)
  }
}
