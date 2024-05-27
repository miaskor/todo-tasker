package by.miaskor.token.controller

import by.miaskor.domain.connector.ClientConnector
import by.miaskor.domain.model.client.ClientRequest
import by.miaskor.token.connector.domain.ClientAuthDtoRequest
import by.miaskor.token.connector.domain.TokenDto
import by.miaskor.token.security.TokenProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/")
class TokenController(
  private val tokenProvider: TokenProvider,
  private val clientConnector: ClientConnector,
) {
  @PostMapping("/create/token")
  fun createToken(@RequestBody clientAuthDtoRequest: ClientAuthDtoRequest): ResponseEntity<Map<String, String>> {
    val client = clientConnector.getBy(
      ClientRequest(
        login = clientAuthDtoRequest.login,
        password = clientAuthDtoRequest.password
      )
    )

    val token = tokenProvider.createToken(clientAuthDtoRequest)
    return ResponseEntity.ok(
      mapOf(
        Pair("name", clientAuthDtoRequest.login),
        Pair("token", token)
      )
    )
  }

  @PostMapping("/validate/token")
  fun validateToken(@RequestBody tokenDto: TokenDto): ResponseEntity<Boolean> {
    return if (tokenProvider.validateToken(tokenDto.token)) {
      ResponseEntity.ok(true)
    } else {
      ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }
  }
}
