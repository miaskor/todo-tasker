package by.miaskor.domain.service

import by.miaskor.domain.api.exception.ClientNotFoundException
import by.miaskor.domain.api.exception.EmailAlreadyOccupiedException
import by.miaskor.domain.api.exception.LoginAlreadyOccupiedException
import by.miaskor.domain.model.client.ClientRequest
import by.miaskor.domain.model.client.CreateClientRequest
import by.miaskor.domain.store.entity.ClientEntity
import by.miaskor.domain.store.repository.ClientRepository
import by.miaskor.domain.store.repository.specification.ClientSpecificationFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class ClientService(
  private val clientRepository: ClientRepository,
  private val passwordEncoder: PasswordEncoder,
  private val clientSpecificationFactory: ClientSpecificationFactory,
) {

  open fun create(createClientRequest: CreateClientRequest): ClientEntity {
    val (email, login, password, botId) = createClientRequest

    clientRepository.findByEmailOrLogin(email, login)
      .ifPresent { clientEntity -> throwExceptionIfExists(clientEntity, createClientRequest) }

    return ClientEntity(
      email = email,
      login = login,
      password = passwordEncoder.encode(password),
      botId = botId
    )
      .also(clientRepository::saveAndFlush)
  }

  private fun throwExceptionIfExists(clientEntity: ClientEntity, createClientRequest: CreateClientRequest) {
    when {
      clientEntity.login == createClientRequest.login -> throw LoginAlreadyOccupiedException(createClientRequest.login)
      clientEntity.email == createClientRequest.email -> throw EmailAlreadyOccupiedException(createClientRequest.email)
    }
  }

  @Transactional(readOnly = true)
  open fun search(clientRequest: ClientRequest): ClientEntity {
    if (clientRequest.isEmpty()) throw ClientNotFoundException()

    return clientSpecificationFactory.createFindAllBy(clientRequest)
      .let(clientRepository::findOne)
      .orElseThrow { throw ClientNotFoundException("object" to clientRequest) }
  }

  @Transactional(readOnly = true)
  open fun getById(id: Long): ClientEntity {
    return clientRepository.findById(id)
      .orElseThrow { ClientNotFoundException("id" to id) }
  }

  open fun updateById(id: Long, clientRequest: ClientRequest): ClientEntity {
    if (clientRequest.isEmpty()) throw ClientNotFoundException("object" to clientRequest)

    val clientEntity = getById(id)
      .let {
        ClientEntity(
          id = id,
          email = clientRequest.email ?: it.email,
          login = clientRequest.login ?: it.login,
          botId = clientRequest.botId ?: it.botId,
          password = if (clientRequest.password != null) passwordEncoder.encode(clientRequest.password) else it.password
        )
      }

    clientRepository.saveAndFlush(clientEntity)

    return clientEntity
  }

  open fun getAndDelete(id: Long) {
    getById(id)
      .also { clientRepository.deleteById(id) }
  }
}