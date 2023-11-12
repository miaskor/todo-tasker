package by.hariton;

import by.hariton.command.CommandProcessor;
import by.hariton.command.CommandProcessorRegistry;
import by.hariton.config.properties.BotProperties;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@AllArgsConstructor
public class ToDoTaskerBot extends TelegramLongPollingBot {

  private final CommandResolver commandResolver;
  private final BotProperties botProperties;
  private final CommandProcessorRegistry commandProcessorRegistry;

  @SneakyThrows
  public void onUpdateReceived(Update update) {
    if (!update.hasMessage()) {
      return;
    }
    Message message = update.getMessage();
    String command = commandResolver.resolve(message);
    String chatId = message.getChatId().toString();

    CommandProcessor<?> commandProcessor = commandProcessorRegistry.lookup(command);
    BotApiMethod<?> response = commandProcessor.process(chatId, command);
    sendResponse(response);
  }

  @SneakyThrows
  public void sendResponse(BotApiMethod<?> response) {
    execute(response);
  }

  public String getBotUsername() {
    return botProperties.username();
  }

  public String getBotToken() {
    return botProperties.token();
  }
}
