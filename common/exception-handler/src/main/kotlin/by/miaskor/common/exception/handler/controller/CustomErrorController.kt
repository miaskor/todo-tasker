package by.miaskor.common.exception.handler.controller

import by.miaskor.common.exception.handler.model.ErrorResult
import by.miaskor.common.exception.handler.model.Result
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.context.request.WebRequest

@Controller
class CustomErrorController(
  private val errorAttributes: ErrorAttributes,
) : ErrorController {

  @RequestMapping("/error")
  fun error(webRequest: WebRequest): ResponseEntity<Result<*>> {
    val attributes = errorAttributes.getErrorAttributes(
      webRequest,
      ErrorAttributeOptions.of(
        ErrorAttributeOptions.Include.EXCEPTION,
        ErrorAttributeOptions.Include.MESSAGE
      )
    )
    val statusCode = attributes["status"] as Int
    val errorMessage = attributes["message"] as String
    val result = Result<Any>(
      error = ErrorResult(
        code = statusCode,
        detail = errorMessage
      )
    )
    return ResponseEntity
      .status(statusCode)
      .body(result)
  }
}
