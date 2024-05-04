package by.miaskor.domain.store.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "client")
open class ClientEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  open val id: Long = -1,
  open val email: String = "",
  open val login: String = "",
  @Column(nullable = true)
  open val botId: Long? = null,
  open val password: String = "",
){

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ClientEntity

    if (id != other.id) return false
    if (email != other.email) return false
    if (login != other.login) return false
    if (botId != other.botId) return false
    if (password != other.password) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + email.hashCode()
    result = 31 * result + login.hashCode()
    result = 31 * result + (botId?.hashCode() ?: 0)
    result = 31 * result + password.hashCode()
    return result
  }

  override fun toString(): String {
    return "ClientEntity(id=$id, email='$email', login='$login', botId=$botId)"
  }
}
