package by.hariton.command;

import static by.hariton.SendMessageFactory.createSendMessage;

import by.hariton.config.properties.BotMessageProperties;
import by.miaskor.domain.connector.TaskConnector;
import by.miaskor.domain.model.task.TaskResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
public class TaskStateCommandProcessor implements CommandProcessor<Message> {

  private final BotMessageProperties botMessageProperties;
  private final TaskConnector taskConnector;

  @Override
  @SneakyThrows
  public SendMessage process(String chatId, String command) {
    String taskState = command;
    List<TaskResponse> taskDtos = taskConnector.getAllByBotIdAndState(Long.parseLong(chatId), taskState);

    if (taskDtos.isEmpty()) {
      return sendMessageIfTaskNotExists(chatId, taskState);
    } else {
      return sendMessageIfTaskExists(chatId, taskState, taskDtos);
    }
  }

  private SendMessage sendMessageIfTaskNotExists(String chatId, String taskState) {
    String message = botMessageProperties.taskWithStateIsNotExists().formatted(taskState);

    return createSendMessage(chatId, message);
  }

  private SendMessage sendMessageIfTaskExists(String chatId, String taskState, List<TaskResponse> taskDtos) {
    String tasks = taskDtos.stream()
        .map(TaskResponse::getTaskName)
        .collect(Collectors.joining("\n"));

    String message = botMessageProperties.taskWithStateExists().formatted(taskState, tasks);
    return createSendMessage(chatId, message);
  }
}
