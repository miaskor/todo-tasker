package by.miaskor.domain.api.exception

class EmailAlreadyOccupiedException(email: String, cause: Throwable? = null) :
  BadRequestException("Email |$email| is already occupied", cause)