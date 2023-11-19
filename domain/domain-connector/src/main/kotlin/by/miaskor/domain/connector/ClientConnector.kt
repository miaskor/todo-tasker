package by.miaskor.domain.connector

import by.miaskor.domain.model.client.ClientResponse
import by.miaskor.domain.model.client.CreateClientRequest
import feign.Headers
import feign.Param
import feign.RequestLine

@Headers(value = ["Content-type: application/json"])
interface ClientConnector {

  @RequestLine("GET /auth?login={login}&password={password}")
  fun getByLoginAndPassword(@Param("login") login: String, @Param("password") password: String):
      ClientResponse

  @RequestLine("GET ?login={login}")
  fun getByLogin(@Param("login") login: String): ClientResponse

  @RequestLine("POST /")
  fun createClient(createClientRequest: CreateClientRequest): ClientResponse

  @RequestLine("POST /byBotId/{bot_id}")
  fun getByBotId(@Param("bot_id") botId: Long): ClientResponse

  @RequestLine("GET /{id}")
  fun getById(@Param("id") clientId: Int): ClientResponse

  @RequestLine("PATCH /{id}")
  fun update(@Param("id") clientId: Int, createClientRequest: CreateClientRequest): ClientResponse
}
