package by.miaskor.domain.api.exception

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(BAD_REQUEST)
open class BadRequestException(message: String?, cause: Throwable? = null) : RuntimeException(message, cause)
