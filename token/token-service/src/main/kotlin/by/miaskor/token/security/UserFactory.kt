package by.miaskor.token.security

import by.miaskor.domain.dto.ClientDtoResponse

class UserFactory {

  fun create(clientDtoResponse: ClientDtoResponse): User {
    return User(
      id = clientDtoResponse.id,
      login = clientDtoResponse.login,
      email = clientDtoResponse.email,
      user_password = ""
    )
  }
}
