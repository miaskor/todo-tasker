package by.hariton.command;

import by.hariton.SendMessageFactory;
import by.hariton.config.properties.BotMessageProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
public class IncorrectCommandProcessor implements CommandProcessor<Message> {

  private BotMessageProperties botMessageProperties;

  @Override
  public SendMessage process(String chatId, String command) {
    return SendMessageFactory.createSendMessage(chatId, botMessageProperties.incorrectCommand());
  }
}
