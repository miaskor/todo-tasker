package by.hariton.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@ConfigurationProperties("bot")
@ConstructorBinding
public record BotProperties(
    String username,
    String token
) {

}
