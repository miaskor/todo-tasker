package by.hariton.command;

import static by.hariton.SendMessageFactory.createSendMessage;

import by.hariton.config.properties.BotMessageProperties;
import by.miaskor.domain.connector.TaskConnector;
import by.miaskor.domain.dto.TaskDtoResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
public class DateCommandProcessor implements CommandProcessor<Message> {

  private final BotMessageProperties botMessageProperties;
  private final TaskConnector taskConnector;

  @Override
  @SneakyThrows
  public SendMessage process(String chatId, String command) {
    String date = command;
    List<TaskDtoResponse> taskDtos = taskConnector.getAllByBotIdAndDate(Long.parseLong(chatId), date);

    if (taskDtos.isEmpty()) {
      return sendMessageIfTaskNotExists(chatId, date);
    } else {
      return sendMessageIfTaskExists(chatId, date, taskDtos);
    }
  }

  private SendMessage sendMessageIfTaskNotExists(String chatId, String date) {
    String message = botMessageProperties.taskIsNotExists().formatted(date);

    return createSendMessage(chatId, message);
  }

  private SendMessage sendMessageIfTaskExists(String chatId, String date, List<TaskDtoResponse> taskDtos) {
    String tasks = taskDtos.stream()
        .map(TaskDtoResponse::getTaskName)
        .collect(Collectors.joining("\n"));

    String message = botMessageProperties.taskExists().formatted(date, tasks);
    return createSendMessage(chatId, message);
  }
}
