package by.miaskor.token.config

import by.miaskor.domain.connector.ClientConnector
import by.miaskor.token.error.decoder.ClientFeignErrorDecoder
import feign.Feign
import feign.Logger
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import feign.okhttp.OkHttpClient
import feign.slf4j.Slf4jLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ConnectorConfig {

  @Bean
  open fun clientConnector(): ClientConnector {
    return Feign.builder()
      .client(OkHttpClient())
      .errorDecoder(ClientFeignErrorDecoder())
      .decoder(GsonDecoder())
      .encoder(GsonEncoder())
      .logger(Slf4jLogger(ClientConnector::class.java))
      .logLevel(Logger.Level.FULL)
      .target(ClientConnector::class.java, "http://domain-api:8080/api/clients")
  }
}
