package by.hariton.command;

import static by.hariton.SendMessageFactory.createSendMessage;

import by.hariton.config.properties.BotMessageProperties;
import by.miaskor.domain.connector.TaskConnector;
import by.miaskor.domain.model.task.SearchTaskRequest;
import by.miaskor.domain.model.task.TaskResponse;
import by.miaskor.domain.model.task.TaskState;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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
    List<TaskResponse> taskDtos = taskConnector.getBy(
        new SearchTaskRequest(
            Long.parseLong(chatId),
            Long.valueOf(-1),
            TaskState.FAILED,
            LocalDate.parse(date),
            LocalDate.now()
        )
    );
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

  private SendMessage sendMessageIfTaskExists(String chatId, String date, List<TaskResponse> taskDtos) {
    String tasks = taskDtos.stream()
        .map(TaskResponse::getTaskName)
        .collect(Collectors.joining("\n"));

    String message = botMessageProperties.taskExists().formatted(date, tasks);
    return createSendMessage(chatId, message);
  }
}
