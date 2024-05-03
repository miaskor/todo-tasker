package by.miaskor.domain.connector

import by.miaskor.domain.model.client.ClientRequest
import by.miaskor.domain.model.client.ClientResponse
import by.miaskor.domain.model.client.CreateClientRequest
import feign.Headers
import feign.Param
import feign.RequestLine

@Headers(value = ["Content-type: application/json"])
interface ClientConnector {

  @RequestLine("POST /search")
  fun getBy(clientRequest: ClientRequest): ClientResponse

  @RequestLine("POST /")
  fun createClient(createClientRequest: CreateClientRequest): ClientResponse

  @RequestLine("GET /{id}")
  fun getById(@Param("id") clientId: Long): ClientResponse

  @RequestLine("PATCH /{id}")
  fun update(@Param("id") clientId: Long, clientRequest: ClientRequest): ClientResponse

  @RequestLine("DELETE /{id}")
  fun delete(@Param("id") clientId: Long)
}
