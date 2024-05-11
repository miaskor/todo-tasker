package by.miaskor.domain.connector

import by.miaskor.domain.model.Result
import by.miaskor.domain.model.client.ClientRequest
import by.miaskor.domain.model.client.ClientResponse
import by.miaskor.domain.model.client.CreateClientRequest
import feign.Headers
import feign.Param
import feign.RequestLine
import feign.hystrix.FallbackFactory

@Headers(value = ["Content-type: application/json"])
interface ClientConnector {

  @RequestLine("POST /search")
  fun getBy(clientRequest: ClientRequest): Result<ClientResponse>

  @RequestLine("POST /")
  fun createClient(createClientRequest: CreateClientRequest): Result<ClientResponse>

  @RequestLine("GET /{id}")
  fun getById(@Param("id") clientId: Long): Result<ClientResponse>

  @RequestLine("PATCH /{id}")
  fun update(@Param("id") clientId: Long, clientRequest: ClientRequest): Result<ClientResponse>

  @RequestLine("DELETE /{id}")
  fun delete(@Param("id") clientId: Long)

  class ClientConnectorFallbackFactory : FallbackFactory<ClientConnector> {
    override fun create(p0: Throwable?): ClientConnector {
      return object : ClientConnector {
        override fun getBy(clientRequest: ClientRequest): Result<ClientResponse> {
          return Result()
        }

        override fun createClient(createClientRequest: CreateClientRequest): Result<ClientResponse> {
          return Result()
        }

        override fun getById(clientId: Long): Result<ClientResponse> {
          return Result()
        }

        override fun update(clientId: Long, clientRequest: ClientRequest): Result<ClientResponse> {
          return Result()
        }

        override fun delete(clientId: Long) {
        }
      }
    }
  }
}


