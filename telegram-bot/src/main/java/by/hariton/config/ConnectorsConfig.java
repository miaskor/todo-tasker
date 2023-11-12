package by.hariton.config;

import by.hariton.command.TaskStateCommandProcessor;
import by.hariton.config.properties.BotMessageProperties;
import by.miaskor.domain.connector.ClientConnector;
import by.miaskor.domain.connector.TaskConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@AllArgsConstructor
public class ConnectorsConfig {

  private final Environment environment;

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper().registerModule(new JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  @Bean
  public ClientConnector clientConnector() {
    String urlClient = environment.getProperty("connector.rest-api.client-url");

    return Feign.builder()
        .client(new OkHttpClient())
        .decoder(new JacksonDecoder(objectMapper()))
        .encoder(new JacksonEncoder(objectMapper()))
        .logger(new Slf4jLogger(ClientConnector.class))
        .logLevel(Logger.Level.FULL)
        .target(ClientConnector.class, urlClient);
  }

  @Bean
  public TaskConnector taskConnector() {
    String urlTask = environment.getProperty("connector.rest-api.task-url");

    return Feign.builder()
        .client(new OkHttpClient())
        .decoder(new JacksonDecoder(objectMapper()))
        .encoder(new JacksonEncoder(objectMapper()))
        .logger(new Slf4jLogger(TaskConnector.class))
        .logLevel(Logger.Level.FULL)
        .target(TaskConnector.class, urlTask);
  }
}
