package com.miaskor.todo.spring;

import com.miaskor.todo.spring.config.ApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(ApplicationConfiguration.class)
@SpringBootApplication
public class WebappApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebappApplication.class, args);
  }
}
