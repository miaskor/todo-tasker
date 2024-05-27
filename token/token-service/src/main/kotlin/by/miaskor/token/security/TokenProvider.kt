package by.miaskor.token.security

import by.miaskor.token.connector.domain.ClientAuthDtoRequest
import by.miaskor.token.exception.AuthenticationException
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class TokenProvider(
  private val userDetailsService: UserDetailsService,
) {

  fun createToken(clientAuthDtoRequest: ClientAuthDtoRequest): String {
    return JWT.create()
      .withSubject(clientAuthDtoRequest.login)
      .withExpiresAt(Date(System.currentTimeMillis() + EXPIRED_TIME))
      .sign(algorithm)
  }

  private fun getAuthentication(token: String): Authentication {
    val userDetails: UserDetails = userDetailsService.loadUserByUsername(getUsername(token))
    return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
  }

  fun validateToken(token: String): Boolean {
    if (token.isEmpty()) {
      return false
    }
    return try {
      val claims = JWT.require(algorithm).build().verify(token).expiresAt
      getAuthentication(token)
      claims.after(Date())
    } catch (e: Exception) {
      throw AuthenticationException("JWT token is expired or invalid")
    }
  }

  private fun getUsername(token: String): String {
    return JWT.require(algorithm).build().verify(token).subject
  }

  companion object {
    private const val EXPIRED_TIME = 60 * 60 * 1000
    private val algorithm = Algorithm.HMAC256("secret".toByteArray())
  }
}
