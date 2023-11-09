package by.hariton;

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

public class ConnectorsConfig {

    private static final String URL_CLIENT = "http://domain-api:8080/api/clients";
    private static final String URL_TASK = "http://domain-api:8080/api/tasks";

    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public ClientConnector clientConnector() {
        return Feign.builder()
                .client(new OkHttpClient())
                .decoder(new JacksonDecoder(objectMapper()))
                .encoder(new JacksonEncoder(objectMapper()))
                .logger(new Slf4jLogger(ClientConnector.class))
                .logLevel(Logger.Level.FULL)
                .target(ClientConnector.class, URL_CLIENT);
    }

    public TaskConnector taskConnector() {
        return Feign.builder()
                .client(new OkHttpClient())
                .decoder(new JacksonDecoder(objectMapper()))
                .encoder(new JacksonEncoder(objectMapper()))
                .logger(new Slf4jLogger(TaskConnector.class))
                .logLevel(Logger.Level.FULL)
                .target(TaskConnector.class, URL_TASK);
    }
}
