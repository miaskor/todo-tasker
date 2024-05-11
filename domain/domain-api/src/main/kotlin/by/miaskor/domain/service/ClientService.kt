package by.miaskor.domain.service

import by.miaskor.domain.api.exception.ClientNotFoundException
import by.miaskor.domain.api.exception.EmailAlreadyOccupiedException
import by.miaskor.domain.api.exception.LoginAlreadyOccupiedException
import by.miaskor.domain.model.client.ClientRequest
import by.miaskor.domain.model.client.CreateClientRequest
import by.miaskor.domain.store.entity.ClientEntity
import by.miaskor.domain.store.repository.ClientRepository
import by.miaskor.domain.store.repository.specification.ClientSpecificationFactory
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
open class ClientService(
  private val clientRepository: ClientRepository,
  private val passwordEncoder: PasswordEncoder,
  private val clientSpecificationFactory: ClientSpecificationFactory,
) {

  open fun create(createClientRequest: CreateClientRequest): ClientEntity {
    val (email, login, password, botId) = createClientRequest

    clientSpecificationFactory.createFindAllBy(ClientRequest(login = login, email = email))
      .let(clientRepository::findOne)
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

    val existedEntity = getById(id)
    val clientEntity = ClientEntity(
      id = id,
      email = clientRequest.email ?: existedEntity.email,
      login = clientRequest.login ?: existedEntity.login,
      botId = clientRequest.botId ?: existedEntity.botId,
      password = Optional.ofNullable(clientRequest.password)
        .map(passwordEncoder::encode)
        .orElse(existedEntity.password)
    )
    try {
      clientRepository.saveAndFlush(clientEntity)
    } catch (exception: DataIntegrityViolationException) {
      val constraintViolationException = exception.cause as ConstraintViolationException

      when {
        constraintViolationException.sqlException.message?.contains("email") == true ->
          throw EmailAlreadyOccupiedException(existedEntity.email, exception.cause)

        constraintViolationException.sqlException.message?.contains("login") == true ->
          throw LoginAlreadyOccupiedException(existedEntity.login, exception.cause)

        else -> throw exception
      }
    }


    return clientEntity
  }

  open fun delete(id: Long) {
    try {
      clientRepository.deleteById(id)
    } catch (exception: EmptyResultDataAccessException) {
      throw ClientNotFoundException("id" to id)
    }
  }
}