package by.hariton;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMessageFactory {

  public static SendMessage createSendMessage(String chatId, String text) {
    return SendMessage.builder()
        .text(text)
        .chatId(chatId)
        .build();
  }
}
