package by.miaskor.token.security

import by.miaskor.domain.model.client.ClientResponse

class UserFactory {

  fun create(clientResponse: ClientResponse): SecurityClient {
    return SecurityClient(clientResponse)
  }
}
