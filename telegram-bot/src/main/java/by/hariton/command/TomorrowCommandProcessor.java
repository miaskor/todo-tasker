package by.hariton.command;

import static by.hariton.SendMessageFactory.createSendMessage;

import by.hariton.config.properties.BotMessageProperties;
import by.miaskor.domain.connector.TaskConnector;
import by.miaskor.domain.model.task.TaskResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
public class TomorrowCommandProcessor implements CommandProcessor<Message> {

  private final BotMessageProperties botMessageProperties;
  private final TaskConnector taskConnector;

  @Override
  @SneakyThrows
  public SendMessage process(String chatId, String command) {
    List<TaskResponse> taskDtos = taskConnector.getTasksOnTomorrowByBotId(Long.parseLong(chatId));

    if (taskDtos.isEmpty()) {
      return sendMessageIfTaskNotExists(chatId);
    } else {
      return sendMessageIfTaskExists(chatId, taskDtos);
    }
  }

  private SendMessage sendMessageIfTaskNotExists(String chatId) {
    String tomorrow = LocalDate.now().plusDays(1).toString();
    String message = botMessageProperties.taskIsNotExists().formatted(tomorrow);

    return createSendMessage(chatId, message);
  }

  private SendMessage sendMessageIfTaskExists(String chatId, List<TaskResponse> taskDtos) {
    String tomorrow = LocalDate.now().plusDays(1).toString();
    String tasks = taskDtos.stream()
        .map(TaskResponse::getTaskName)
        .collect(Collectors.joining("\n"));

    String message = botMessageProperties.taskExists().formatted(tomorrow, tasks);
    return createSendMessage(chatId, message);
  }
}
