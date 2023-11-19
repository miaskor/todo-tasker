package by.miaskor.token.security

import by.miaskor.domain.model.client.ClientResponse

class UserFactory {

  fun create(clientResponse: ClientResponse): User {
    return User(
      id = clientResponse.id,
      login = clientResponse.login,
      email = clientResponse.email,
      user_password = ""
    )
  }
}
