package by.miaskor.domain.api.exception

class LoginAlreadyOccupiedException(login: String) : RuntimeException("Login |$login| is already occupied") {
}