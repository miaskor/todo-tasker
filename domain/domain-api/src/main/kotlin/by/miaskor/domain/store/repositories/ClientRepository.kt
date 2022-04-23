package by.miaskor.domain.store.repositories

import by.miaskor.domain.store.entities.ClientEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ClientRepository : JpaRepository<ClientEntity, Int> {
  fun findByEmailOrLogin(email: String, login: String): Optional<ClientEntity>
  fun findByBotId(botId: Long): Optional<ClientEntity>
  fun findByLogin(login: String): Optional<ClientEntity>
  fun findByEmail(email: String): Optional<ClientEntity>
}
