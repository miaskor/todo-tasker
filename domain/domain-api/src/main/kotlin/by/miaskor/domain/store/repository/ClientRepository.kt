package by.miaskor.domain.store.repository

import by.miaskor.domain.store.entity.ClientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ClientRepository : JpaRepository<ClientEntity, Long>, JpaSpecificationExecutor<ClientEntity> {
  fun findByEmailOrLogin(email: String?, login: String?): Optional<ClientEntity>
}
