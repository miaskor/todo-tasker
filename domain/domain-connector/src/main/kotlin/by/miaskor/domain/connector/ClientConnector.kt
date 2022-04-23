package by.miaskor.domain.connector

import by.miaskor.domain.dto.ClientDtoRequest
import by.miaskor.domain.dto.ClientDtoResponse
import feign.Headers
import feign.Param
import feign.RequestLine

@Headers(value = ["Content-type: application/json"])
interface ClientConnector {

  @RequestLine("GET /auth?login={login}&password={password}")
  fun getByLoginAndPassword(@Param("login") login: String, @Param("password") password: String):
      ClientDtoResponse

  @RequestLine("GET ?login={login}")
  fun getByLogin(@Param("login") login: String): ClientDtoResponse

  @RequestLine("POST /")
  fun createClient(clientDtoRequest: ClientDtoRequest): ClientDtoResponse

  @RequestLine("POST /byBotId/{bot_id}")
  fun getByBotId(@Param("bot_id") botId: Long): ClientDtoResponse

  @RequestLine("GET /{id}")
  fun getById(@Param("id") clientId: Int): ClientDtoResponse

  @RequestLine("PATCH /{id}")
  fun update(@Param("id") clientId: Int, clientDtoRequest: ClientDtoRequest): ClientDtoResponse
}
