package by.miaskor.domain.api.controllers

import by.miaskor.domain.api.exceptions.BadRequestException
import by.miaskor.domain.api.exceptions.NotFoundException
import by.miaskor.domain.dto.ClientDtoRequest
import by.miaskor.domain.dto.ClientDtoResponse
import by.miaskor.domain.factories.ClientDtoResponseFactory
import by.miaskor.domain.store.entities.ClientEntity
import by.miaskor.domain.store.repositories.ClientRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api")
open class ClientController(
  private val clientRepository: ClientRepository,
  private val clientDtoResponseFactory: ClientDtoResponseFactory,
  private val passwordEncoder: PasswordEncoder
) {

  @PostMapping(CREATE_CLIENT)
  fun createClient(@RequestBody clientDtoRequest: ClientDtoRequest): ResponseEntity<ClientDtoResponse> {
    checkValueBlank("Login", clientDtoRequest.login)
    checkValueBlank("Password", clientDtoRequest.password ?: "")
    checkValueBlank("Email", clientDtoRequest.email)

    clientRepository.findByEmail(clientDtoRequest.email)
      .ifPresent {
        throw BadRequestException(
          "Client with email = ${clientDtoRequest.email}  already exists"
        )
      }
    clientRepository.findByLogin(clientDtoRequest.login)
      .ifPresent {
        throw BadRequestException(
          "Client with login = ${clientDtoRequest.login} already exists"
        )
      }
    val encodedPassword = passwordEncoder.encode(clientDtoRequest.password)
    val clientEntity = clientRepository.saveAndFlush(
      ClientEntity(
        email = clientDtoRequest.email,
        login = clientDtoRequest.login,
        password = encodedPassword
      )
    )
    return ResponseEntity.ok(clientDtoResponseFactory.makeClientDtoResponse(clientEntity))
  }

  @GetMapping(GET_CLIENT)
  fun getClient(@PathVariable id: Int): ResponseEntity<ClientDtoResponse> {
    val clientEntity = clientRepository.findById(id)
      .orElseThrow { NotFoundException("Client with id $id not exists") }
    return ResponseEntity.ok(
      clientDtoResponseFactory.makeClientDtoResponse(clientEntity)
    )
  }

  @GetMapping(GET_CLIENT_BY_BOT_ID)
  fun getClientByBotId(@PathVariable("id") botId: Long): ResponseEntity<ClientDtoResponse> {
    val clientEntity = clientRepository.findByBotId(botId)
      .orElseThrow { NotFoundException("Client with bot id $botId not exists or not used") }
    return ResponseEntity.ok(
      clientDtoResponseFactory.makeClientDtoResponse(clientEntity)
    )
  }

  @GetMapping(GET_CLIENT_BY_LOGIN_OR_EMAIL)
  fun getClientByLoginOrEmail(
    @RequestParam("login") login: Optional<String>, @RequestParam("email") email:
    Optional<String>
  ): ResponseEntity<ClientDtoResponse> {
    var clientEntity: ClientEntity? = null
    login.ifPresent {
      checkValueBlank("Login", it)
      clientEntity = clientRepository.findByLogin(it)
        .orElseThrow { NotFoundException("Client with login $it not exists") }
    }
    if (clientEntity != null) {
      return ResponseEntity.ok(
        clientDtoResponseFactory.makeClientDtoResponse(clientEntity ?: ClientEntity())
      )
    }
    email.ifPresent {
      checkValueBlank("Email", it)
      clientEntity = clientRepository.findByEmail(it)
        .orElseThrow { NotFoundException("Client with email $it not exists") }
    }
    return ResponseEntity.ok(
      clientDtoResponseFactory.makeClientDtoResponse(clientEntity ?: ClientEntity())
    )
  }

  @GetMapping(GET_CLIENT_BY_LOGIN_AND_PASSWORD)
  fun getClientByLoginAndPassword(@RequestParam("login") login: String, @RequestParam("password") password: String):
      ResponseEntity<ClientDtoResponse> {
    checkValueBlank("Login", login)
    checkValueBlank("Password", password)
    val clientEntity = clientRepository.findByLogin(login)
      .orElseThrow { NotFoundException("Client with login $login not exists") }
    if (!passwordEncoder.matches(password, clientEntity.password)) {
      throw NotFoundException("Client with login $login and password $password not exists")
    }
    return ResponseEntity.ok(
      clientDtoResponseFactory.makeClientDtoResponse(clientEntity)
    )
  }

  @PatchMapping(UPDATE_CLIENT)
  fun update(
    @PathVariable("id") clientId: Int,
    @RequestBody clientDtoRequest: ClientDtoRequest
  ): ResponseEntity<ClientDtoResponse> {
    clientRepository.findById(clientId)
      .orElseThrow { NotFoundException("Client with clientId $clientId not exists") }
    val encodedPassword = passwordEncoder.encode(clientDtoRequest.password)
    val clientEntity = clientRepository.saveAndFlush(
      ClientEntity(
        id = clientId,
        email = clientDtoRequest.email,
        login = clientDtoRequest.login,
        botId = clientDtoRequest.botId,
        password = encodedPassword
      )
    )
    return ResponseEntity.ok(
      clientDtoResponseFactory.makeClientDtoResponse(clientEntity)
    )
  }

  @DeleteMapping(DELETE_CLIENT)
  fun delete(@PathVariable("id") clientId: Int) {
    clientRepository.findById(clientId)
      .orElseThrow { NotFoundException("Client with id $clientId not exists") }
    clientRepository.deleteById(clientId)
  }

  private fun checkValueBlank(nameField: String, field: String) {
    if (field.trim().isBlank()) {
      throw BadRequestException("$nameField $field is can not be empty")
    }
  }

  companion object {
    private const val CREATE_CLIENT = "/clients"
    private const val GET_CLIENT = "/clients/{id}"
    private const val DELETE_CLIENT = "/clients/{id}"
    private const val GET_CLIENT_BY_LOGIN_OR_EMAIL = "/clients"
    private const val GET_CLIENT_BY_LOGIN_AND_PASSWORD = "/clients/auth"
    private const val GET_CLIENT_BY_BOT_ID = "/clients/byBotId/{id}"
    private const val UPDATE_CLIENT = "/clients/{id}"
  }
}
