package by.miaskor.domain.store.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "client")
data class ClientEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Int = -1,
  val email: String = "",
  val login: String = "",
  @Column(nullable = true)
  val botId: Long? = null,
  val password: String = ""
)
