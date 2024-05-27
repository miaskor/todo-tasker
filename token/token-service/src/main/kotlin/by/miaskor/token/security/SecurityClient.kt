package by.miaskor.token.security

import by.miaskor.domain.model.client.ClientResponse
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class SecurityClient(
  val clientResponse: ClientResponse,
) : UserDetails {

  override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
    return mutableListOf(clientResponse.type)
  }

  override fun getPassword(): String {
    return clientResponse.password
  }

  override fun getUsername(): String {
    return clientResponse.login
  }

  override fun isAccountNonExpired(): Boolean {
    return clientResponse.blocked.not()
  }

  override fun isAccountNonLocked(): Boolean {
    return clientResponse.blocked.not()
  }

  override fun isCredentialsNonExpired(): Boolean {
    return clientResponse.blocked.not()
  }

  override fun isEnabled(): Boolean {
    return clientResponse.blocked.not()
  }
}

