package by.miaskor.domain.api.exception

class EmailAlreadyOccupiedException(email: String) : BadRequestException("Email |$email| is already occupied")