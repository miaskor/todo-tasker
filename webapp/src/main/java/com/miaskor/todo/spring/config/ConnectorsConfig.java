package com.miaskor.todo.spring.config;

import by.miaskor.domain.connector.ClientConnector;
import by.miaskor.domain.connector.TaskConnector;
import by.miaskor.domain.dto.ClientDtoRequest;
import by.miaskor.domain.dto.ClientDtoResponse;
import by.miaskor.domain.dto.TaskDtoRequest;
import by.miaskor.domain.dto.TaskDtoResponse;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
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
        .target(ClientConnector.class, urlDomainClientConnector, new ClientConnector() {
          @NotNull
          @Override
          public ClientDtoResponse update(int clientId, @NotNull ClientDtoRequest clientDtoRequest) {
            return new ClientDtoResponse();
          }

          @NotNull
          @Override
          public ClientDtoResponse getById(int clientId) {
            return new ClientDtoResponse();
          }

          @NotNull
          @Override
          public ClientDtoResponse getByBotId(long botId) {
            return new ClientDtoResponse();
          }

          @NotNull
          @Override
          public ClientDtoResponse getByLoginAndPassword(@NotNull String login, @NotNull String password) {
            return new ClientDtoResponse();
          }

          @NotNull
          @Override
          public ClientDtoResponse getByLogin(@NotNull String login) {
            return new ClientDtoResponse();
          }

          @NotNull
          @Override
          public ClientDtoResponse createClient(@NotNull ClientDtoRequest clientDtoRequest) {
            return new ClientDtoResponse();
          }
        });
  }

  @Bean
  public TaskConnector taskConnector() {
    return HystrixFeign.builder()
        .client(new OkHttpClient())
        .decoder(new JacksonDecoder(objectMapper()))
        .encoder(new JacksonEncoder(objectMapper()))
        .logger(new Slf4jLogger(TaskConnector.class))
        .logLevel(Logger.Level.FULL)
        .target(TaskConnector.class, urlDomainTaskConnector, new TaskConnector() {
          @NotNull
          @Override
          public List<TaskDtoResponse> getAllByBotIdAndState(long botId, @NotNull String state) {
            return new ArrayList<>();
          }

          @NotNull
          @Override
          public List<TaskDtoResponse> getAllByBotIdAndDate(long botId, @NotNull String date) {
            return new ArrayList<>();
          }

          @NotNull
          @Override
          public List<TaskDtoResponse> getTasksOnTomorrowByBotId(long botId) {
            return new ArrayList<>();
          }

          @NotNull
          @Override
          public List<TaskDtoResponse> getTasksOnCurrentDayByBotId(long botId) {
            return new ArrayList<>();
          }

          @NotNull
          @Override
          public List<TaskDtoResponse> getAllByClientId(int clientId) {
            return new ArrayList<>();
          }

          @NotNull
          @Override
          public TaskDtoResponse create(@NotNull TaskDtoRequest task) {
            return new TaskDtoResponse();
          }

          @NotNull
          @Override
          public Map<String, List<TaskDtoResponse>> getAllByClientIdAndDateBetween(@NotNull String dateFrom,
              @NotNull String dateTo, int clientId) {
            return new HashMap<>();
          }

          @NotNull
          @Override
          public List<TaskDtoResponse> getAllByClientIdAndDate(@NotNull String date, int clientId) {
            return new ArrayList<>();
          }

          @NotNull
          @Override
          public TaskDtoResponse update(int taskId, @NotNull TaskDtoRequest task) {
            return new TaskDtoResponse();
          }

          @Override
          public void delete(int taskId) {

          }
        });
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
