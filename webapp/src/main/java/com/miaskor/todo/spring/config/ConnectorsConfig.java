package com.miaskor.todo.spring.config;

import by.miaskor.domain.connector.ClientConnector;
import by.miaskor.domain.connector.ClientConnector.ClientConnectorFallbackFactory;
import by.miaskor.domain.connector.TaskConnector;
import by.miaskor.domain.connector.TaskConnector.TaskConnectorFallbackFactory;
import by.miaskor.token.connector.connector.TokenConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miaskor.todo.spring.handler.error.decoder.TokenFeignErrorDecoder;
import feign.Feign;
import feign.Logger;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectorsConfig {

  @Value("${connector.rest-api.client-url}")
  private String urlDomainClientConnector;
  @Value("${connector.rest-api.task-url}")
  private String urlDomainTaskConnector;
  @Value("${connector.token.url}")
  private String urlTokenConnector;

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper().registerModule(new JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  @Bean
  public ClientConnector clientConnector() {
    return HystrixFeign.builder()
        .client(new OkHttpClient())
        .decoder(new JacksonDecoder(objectMapper()))
        .encoder(new JacksonEncoder(objectMapper()))
        .logger(new Slf4jLogger(ClientConnector.class))
        .logLevel(Logger.Level.FULL)
        .target(ClientConnector.class, urlDomainClientConnector, new ClientConnectorFallbackFactory());
  }

  @Bean
  public TaskConnector taskConnector() {
    return HystrixFeign.builder()
        .client(new OkHttpClient())
        .decoder(new JacksonDecoder(objectMapper()))
        .encoder(new JacksonEncoder(objectMapper()))
        .logger(new Slf4jLogger(TaskConnector.class))
        .logLevel(Logger.Level.FULL)
        .target(TaskConnector.class, urlDomainTaskConnector, new TaskConnectorFallbackFactory());
  }

  //TODO(Get Exception when token is expired. And user will get exception on web browser)
  @Bean
  public TokenConnector tokenConnector() {
    return Feign.builder()
        .client(new OkHttpClient())
        .errorDecoder(new TokenFeignErrorDecoder())
        .decoder(new JacksonDecoder(objectMapper()))
        .encoder(new JacksonEncoder(objectMapper()))
        .logger(new Slf4jLogger(TokenConnector.class))
        .logLevel(Logger.Level.FULL)
        .target(TokenConnector.class, urlTokenConnector);
  }
}
