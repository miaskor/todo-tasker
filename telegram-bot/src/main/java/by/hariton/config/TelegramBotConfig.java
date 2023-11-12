package by.hariton.config;

import by.hariton.ToDoTaskerBot;
import by.hariton.command.TaskStateCommandProcessor;
import by.hariton.config.properties.BotMessageProperties;
import by.miaskor.domain.connector.TaskConnector;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@AllArgsConstructor
public class TelegramBotConfig {

  private final ToDoTaskerBot toDoTaskerBot;

  @SneakyThrows
  @EventListener(ContextRefreshedEvent.class)
  public void startBot() {
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
    telegramBotsApi.registerBot(toDoTaskerBot);
  }
}
