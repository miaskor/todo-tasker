package by.miaskor.token.config

import by.miaskor.domain.connector.ClientConnector
import by.miaskor.domain.connector.ClientConnector.ClientConnectorFallbackFactory
import feign.Logger
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import feign.hystrix.HystrixFeign
import feign.okhttp.OkHttpClient
import feign.slf4j.Slf4jLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ConnectorConfig(
  @Value("domain-api-connector.baseURL") private val baseURL: String,
) {

  @Bean
  open fun clientConnector(): ClientConnector {
    return HystrixFeign.builder()
      .client(OkHttpClient())
      .decoder(GsonDecoder())
      .encoder(GsonEncoder())
      .logger(Slf4jLogger(ClientConnector::class.java))
      .logLevel(Logger.Level.FULL)
      .target(ClientConnector::class.java, baseURL, ClientConnectorFallbackFactory())
  }
}
