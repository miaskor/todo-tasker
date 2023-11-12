package by.hariton.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("bot.message")
@ConstructorBinding
public record BotMessageProperties(
   String taskIsNotExists,
   String taskWithStateIsNotExists,
   String taskExists,
   String taskWithStateExists,
   String startMessage,
   String incorrectCommand
) {
}
