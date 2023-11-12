package by.hariton.command;

import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CommandProcessorRegistry {

  private final Map<String, CommandProcessor<?>> commandToCommandProcessor;

  public CommandProcessor<?> lookup(String key) {
    return Optional.ofNullable(commandToCommandProcessor.get(key))
        .orElseThrow(() -> new RuntimeException("CommandProcessor not found by key=%s".formatted(key)));
  }
}
