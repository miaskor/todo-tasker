package by.hariton;

import by.miaskor.domain.connector.TaskConnector;
import by.miaskor.domain.dto.TaskDtoResponse;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class ToDoTaskerBot extends TelegramLongPollingBot {

  public static final String TASKS_IS_NOT_EXISTS_MESSAGE = "Извините, задач cо статусом \"%s\" нет";
  public static final String TASKS_EXISTS_MESSAGE = "Задачи cо статусом \"%s\":\n";
  public static final String START_MESSAGE = """
          Ваш уникальный id - %s.
          Чтобы бот реагировал на ваши сообщения, введите его на сайте, в разделе Profile -> BotId.
          Так же вы можете пользоваться нашими командами или писать любую дату,в формате 2021-01-01, чтобы узнать ваши таски на этот день.
      """;
  private final TaskConnector taskConnector;

  protected ToDoTaskerBot(DefaultBotOptions options) {
    super(options);
    taskConnector = new ConnectorsConfig().taskConnector();
  }

  public String getBotUsername() {
    return "@ToDoTaskerBot";
  }

  public String getBotToken() {
    return "5042012734:AAEoCC7pfoU0uW0GikAomW9jI_j6VbkIDPQ";
  }

  public static void main(String[] args) throws TelegramApiException {
    ToDoTaskerBot bot = new ToDoTaskerBot(new DefaultBotOptions());
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
    telegramBotsApi.registerBot(bot);
  }

  public void onUpdateReceived(Update update) {
    if (update.hasMessage()) {
      try {
        handleMessage(update.getMessage());
      } catch (TelegramApiException e) {
        e.printStackTrace();
      }
    }
  }

  private void handleMessage(Message message) throws TelegramApiException {
    if (message.hasText()) {
      Long botId = message.getChatId();
      List<TaskDtoResponse> taskList;
      String chatId = botId.toString();
      if (message.hasEntities()) {
        Optional<MessageEntity> commandEntity = message.getEntities().stream()
            .filter(e -> "bot_command".equals(e.getType())).findFirst();
        if (commandEntity.isPresent()) {
          String command = message.getText()
              .substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
          switch (command) {
            case "/start" -> execute(createSendMessage(chatId, START_MESSAGE.formatted(chatId)));
            case "/today" -> {
              taskList = taskConnector.getTasksOnCurrentDayByBotId(botId);
              if (taskList.isEmpty()) {
                execute(
                    SendMessage.builder().text("Извините, задач на сегодня нет").chatId(chatId).build());
              } else {
                execute(
                    SendMessage.builder().text("Задачи на сегодня:\n").chatId(chatId).build());
                for (TaskDtoResponse task : taskList) {
                  execute(
                      SendMessage.builder().text(task.getTaskName()).chatId(chatId).build());
                }
              }
            }
            case "/tomorrow" -> {
              taskList = taskConnector.getTasksOnTomorrowByBotId(botId);
              if (taskList.isEmpty()) {
                execute(
                    SendMessage.builder().text("Извините, задач на завтра нет").chatId(chatId).build());
              } else {
                execute(
                    SendMessage.builder().text("Задачи на завтра:\n").chatId(chatId).build());
                for (TaskDtoResponse task : taskList) {
                  execute(
                      SendMessage.builder().text(task.getTaskName()).chatId(chatId).build());
                }
              }
            }
            case "/failed" -> sendMessage(chatId, TaskType.FAILED);
            case "/upcoming" -> sendMessage(chatId, TaskType.UPCOMING);
            case "/completed" -> sendMessage(chatId, TaskType.COMPLETED);
            case "/in_process" -> sendMessage(chatId, TaskType.IN_PROCESS);
          }
        }
      } else {
        try {
          LocalDate.parse(message.getText());
          taskList = taskConnector.getAllByBotIdAndDate(botId, message.getText());
          if (taskList.isEmpty()) {
            execute(
                SendMessage.builder().text("Извините, задач на " + message.getText() + " нет")
                    .chatId(chatId).build());
          } else {
            execute(
                SendMessage.builder().text("Задачи на " + message.getText() + ":\n").chatId(chatId).build());
            for (TaskDtoResponse task : taskList) {
              execute(
                  SendMessage.builder().text(task.getTaskName()).chatId(chatId).build());
            }
          }
        } catch (DateTimeParseException ex) {
          execute(
              SendMessage.builder().text("Введите дату в правильном формате. \n Например: 2021-01-01").chatId(
                  chatId).build());
        }
      }
    }
  }

  private void sendMessage(String chatId, TaskType taskType)
      throws TelegramApiException {
    var taskList = taskConnector.getAllByBotIdAndState(Long.parseLong(chatId), taskType.getName());
    if (taskList.isEmpty()) {
      execute(createSendMessage(chatId, TASKS_IS_NOT_EXISTS_MESSAGE.formatted(taskType.getName())));
    } else {
      execute(createSendMessage(chatId, TASKS_EXISTS_MESSAGE.formatted(taskType.getName())));
      for (TaskDtoResponse task : taskList) {
        execute(createSendMessage(chatId, task.getTaskName()));
      }
    }
  }

  private SendMessage createSendMessage(String chatId, String text) {
    return SendMessage.builder()
        .text(text)
        .chatId(chatId)
        .build();
  }
}
