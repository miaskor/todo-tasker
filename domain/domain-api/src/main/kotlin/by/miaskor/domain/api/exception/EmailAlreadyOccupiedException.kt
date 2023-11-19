package by.miaskor.domain.api.exception

class EmailAlreadyOccupiedException(email: String) : RuntimeException("Email |$email| is already occupied")