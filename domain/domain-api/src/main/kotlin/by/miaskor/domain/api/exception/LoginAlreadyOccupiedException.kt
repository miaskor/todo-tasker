package by.miaskor.domain.api.exception

class LoginAlreadyOccupiedException(login: String) : BadRequestException("Login |$login| is already occupied") {
}