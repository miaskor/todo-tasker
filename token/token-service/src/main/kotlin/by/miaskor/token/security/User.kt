package by.miaskor.token.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class User(
  val id: Long,
  val login: String,
  val email: String,
  val user_password: String,
) : UserDetails {

  override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
    return mutableListOf()
  }

  override fun getPassword(): String {
    return user_password
  }

  override fun getUsername(): String {
    return login
  }

  override fun isAccountNonExpired(): Boolean {
    return true
  }

  override fun isAccountNonLocked(): Boolean {
    return true
  }

  override fun isCredentialsNonExpired(): Boolean {
    return true
  }

  override fun isEnabled(): Boolean {
    return true
  }
}

