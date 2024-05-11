package by.miaskor.domain.api.exception

class LoginAlreadyOccupiedException(login: String, cause: Throwable? = null) :
  BadRequestException("Login |$login| is already occupied", cause)