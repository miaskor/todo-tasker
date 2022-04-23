package by.miaskor.token.connector.connector

import by.miaskor.token.connector.domain.ClientAuthDtoRequest
import by.miaskor.token.connector.domain.TokenDto
import feign.Headers
import feign.RequestLine

@Headers(value = ["Content-type: application/json"])
interface TokenConnector {

  @RequestLine("POST /create/token")
  fun createToken(clientAuthDtoRequest: ClientAuthDtoRequest): Map<String, String>

  @RequestLine("POST /validate/token")
  fun validateToken(tokenDto: TokenDto): Boolean
}
