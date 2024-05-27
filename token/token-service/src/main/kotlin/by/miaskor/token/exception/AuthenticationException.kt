package by.miaskor.token.exception

import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(UNAUTHORIZED)
class AuthenticationException(msg: String) : RuntimeException(msg)
