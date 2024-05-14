package by.miaskor.domain.api.advice

import by.miaskor.domain.model.Result
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import java.util.*

@RestControllerAdvice
class ResponseHandler : ResponseBodyAdvice<Any> {
  override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>) = true

  override fun beforeBodyWrite(
    body: Any?,
    returnType: MethodParameter,
    selectedContentType: MediaType,
    selectedConverterType: Class<out HttpMessageConverter<*>>,
    request: ServerHttpRequest,
    response: ServerHttpResponse,
  ): Any? {
    return if(request.uri.rawPath.contains("actuator")){
      body
    }else{
      Optional.ofNullable(body)
        .filter { it is Result<*> }
        .orElseGet { Result(body) }
    }
  }
}