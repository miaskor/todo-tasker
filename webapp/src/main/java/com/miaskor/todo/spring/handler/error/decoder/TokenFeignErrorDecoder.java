package com.miaskor.todo.spring.handler.error.decoder;

import com.miaskor.todo.spring.handler.ClientNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;

public class TokenFeignErrorDecoder implements ErrorDecoder {

  @Override
  public Exception decode(String methodKey, Response response) {
    StringBuilder builder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(response.body().asReader(Charset.defaultCharset()))) {
      String readStr;
      while ((readStr = reader.readLine()) != null) {
        builder.append(readStr);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new ClientNotFoundException(builder.toString());
  }
}
