package by.miaskor.domain.model.client

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateClientRequest(
  @NotBlank(message = "Email can't be empty")
  @NotNull(message = "Email can't be null")
  val email: String,
  @NotBlank(message = "Login can't be empty")
  @NotNull(message = "Login can't be null")
  val login: String,
  @NotBlank(message = "Password can't be empty")
  @NotNull(message = "Password can't be null")
  val password: String,
  val botId: Long? = null,
) {
  override fun toString(): String {
    return "CreateClientRequest(email='$email', login='$login', botId=$botId)"
  }
}
