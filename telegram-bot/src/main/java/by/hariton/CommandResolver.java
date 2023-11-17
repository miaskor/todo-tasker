package by.hariton;

import java.util.Collections;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

//TODO("date" and "incorrect" are literals)
@Component
public class CommandResolver {

  private static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";
  private static final String BOT_COMMAND = "bot_command";
  private static final String INCORRECT_COMMAND = "incorrect";
  private static final String DATE_COMMAND = "date";

  public String resolve(Message message) {
    return Optional.ofNullable(message.getEntities())
        .orElseGet(Collections::emptyList)
        .stream()
        .filter(e -> BOT_COMMAND.equals(e.getType()))
        .findFirst()
        .map(MessageEntity::getText)
        .map(this::removeSlash)
        .orElseGet(() -> getNotReservedCommand(message));
  }

  private String removeSlash(String text) {
    return text.substring(1);
  }

  private String getNotReservedCommand(Message message) {
    Boolean isDateCommand = Optional.ofNullable(message.getText())
        .map(text -> text.matches(DATE_PATTERN))
        .orElse(false);

    if (isDateCommand) {
      return DATE_COMMAND;
    } else {
      return INCORRECT_COMMAND;
    }
  }
}
