package by.hariton.command;

import static by.hariton.SendMessageFactory.createSendMessage;

import by.hariton.config.properties.BotMessageProperties;
import by.miaskor.domain.connector.TaskConnector;
import by.miaskor.domain.dto.TaskDtoResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
public class TodayCommandProcessor implements CommandProcessor<Message> {

  private final BotMessageProperties botMessageProperties;
  private final TaskConnector taskConnector;

  @Override
  @SneakyThrows
  public SendMessage process(String chatId, String command) {
    List<TaskDtoResponse> taskDtos = taskConnector.getTasksOnCurrentDayByBotId(Long.parseLong(chatId));

    if (taskDtos.isEmpty()) {
      return sendMessageIfTaskNotExists(chatId);
    } else {
      return sendMessageIfTaskExists(chatId, taskDtos);
    }
  }

  private SendMessage sendMessageIfTaskNotExists(String chatId) {
    String today = LocalDate.now().toString();
    String message = botMessageProperties.taskIsNotExists().formatted(today);

    return createSendMessage(chatId, message);
  }

  private SendMessage sendMessageIfTaskExists(String chatId, List<TaskDtoResponse> taskDtos) {
    String today = LocalDate.now().toString();
    String tasks = taskDtos.stream()
        .map(TaskDtoResponse::getTaskName)
        .collect(Collectors.joining("\n"));

    String message = botMessageProperties.taskExists().formatted(today, tasks);
    return createSendMessage(chatId, message);
  }
}
