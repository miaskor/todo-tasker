package by.hariton.command;

import static by.hariton.SendMessageFactory.createSendMessage;

import by.hariton.config.properties.BotMessageProperties;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
public class StartCommandProcessor implements CommandProcessor<Message> {

  private final BotMessageProperties botMessageProperties;

  @Override
  @SneakyThrows
  public SendMessage process(String chatId, String command) {
    String startMessage = botMessageProperties.startMessage();

    return createSendMessage(chatId, startMessage.formatted(chatId));
  }
}
