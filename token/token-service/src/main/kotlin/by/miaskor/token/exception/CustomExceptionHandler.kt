package by.miaskor.token.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class CustomExceptionHandler : ResponseEntityExceptionHandler() {

  @ExceptionHandler(Exception::class)
  fun exception(exception: Exception, webRequest: WebRequest): ResponseEntity<Any>? {
    return handleException(exception, webRequest)
  }
}
