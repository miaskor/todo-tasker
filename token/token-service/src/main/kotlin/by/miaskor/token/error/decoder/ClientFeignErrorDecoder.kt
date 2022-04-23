package by.miaskor.token.error.decoder

import by.miaskor.token.exception.CustomAuthenticationException
import feign.Response
import feign.codec.ErrorDecoder
import java.nio.charset.Charset

class ClientFeignErrorDecoder : ErrorDecoder {
  override fun decode(methodKey: String, response: Response): Exception {
    val body = response.body().asInputStream().bufferedReader(Charset.defaultCharset()).readLine()
    return CustomAuthenticationException(body)
  }
}
