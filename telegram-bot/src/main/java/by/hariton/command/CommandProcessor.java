package by.hariton.command;

import java.io.Serializable;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface CommandProcessor<T extends Serializable> {

  BotApiMethod<T> process(String chatId, String command);
}
