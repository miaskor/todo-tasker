package com.miaskor.todo.spring.config;

import by.miaskor.domain.connector.ClientConnector;
import by.miaskor.domain.connector.TaskConnector;
import by.miaskor.domain.model.client.ClientResponse;
import by.miaskor.domain.model.client.CreateClientRequest;
import by.miaskor.domain.model.task.CreateTaskRequest;
import by.miaskor.domain.model.task.TaskResponse;
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
          public ClientResponse update(int clientId, @NotNull CreateClientRequest createClientRequest) {
            return new ClientResponse();
          }

          @NotNull
          @Override
          public ClientResponse getById(int clientId) {
            return new ClientResponse();
          }

          @NotNull
          @Override
          public ClientResponse getByBotId(long botId) {
            return new ClientResponse();
          }

          @NotNull
          @Override
          public ClientResponse getByLoginAndPassword(@NotNull String login, @NotNull String password) {
            return new ClientResponse();
          }

          @NotNull
          @Override
          public ClientResponse getByLogin(@NotNull String login) {
            return new ClientResponse();
          }

          @NotNull
          @Override
          public ClientResponse createClient(@NotNull CreateClientRequest createClientRequest) {
            return new ClientResponse();
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
          public List<TaskResponse> getAllByBotIdAndState(long botId, @NotNull String state) {
            return new ArrayList<>();
          }

          @NotNull
          @Override
          public List<TaskResponse> getAllByBotIdAndDate(long botId, @NotNull String date) {
            return new ArrayList<>();
          }

          @NotNull
          @Override
          public List<TaskResponse> getTasksOnTomorrowByBotId(long botId) {
            return new ArrayList<>();
          }

          @NotNull
          @Override
          public List<TaskResponse> getTasksOnCurrentDayByBotId(long botId) {
            return new ArrayList<>();
          }

          @NotNull
          @Override
          public List<TaskResponse> getAllByClientId(int clientId) {
            return new ArrayList<>();
          }

          @NotNull
          @Override
          public TaskResponse create(@NotNull CreateTaskRequest task) {
            return new TaskResponse();
          }

          @NotNull
          @Override
          public Map<String, List<TaskResponse>> getAllByClientIdAndDateBetween(@NotNull String dateFrom,
              @NotNull String dateTo, int clientId) {
            return new HashMap<>();
          }

          @NotNull
          @Override
          public List<TaskResponse> getAllByClientIdAndDate(@NotNull String date, int clientId) {
            return new ArrayList<>();
          }

          @NotNull
          @Override
          public TaskResponse update(int taskId, @NotNull CreateTaskRequest task) {
            return new TaskResponse();
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
