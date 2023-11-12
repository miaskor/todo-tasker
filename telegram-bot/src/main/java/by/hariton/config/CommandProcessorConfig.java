package by.hariton.config;

import by.hariton.command.DateCommandProcessor;
import by.hariton.command.IncorrectCommandProcessor;
import by.hariton.command.StartCommandProcessor;
import by.hariton.command.TaskStateCommandProcessor;
import by.hariton.command.TodayCommandProcessor;
import by.hariton.command.TomorrowCommandProcessor;
import by.hariton.config.properties.BotMessageProperties;
import by.miaskor.domain.connector.TaskConnector;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class CommandProcessorConfig {

  private final BotMessageProperties botMessageProperties;
  private final TaskConnector taskConnector;

  @Bean("date")
  public DateCommandProcessor dateCommandProcessor() {
    return new DateCommandProcessor(botMessageProperties, taskConnector);
  }

  @Bean("incorrect")
  public IncorrectCommandProcessor incorrectCommandProcessor() {
    return new IncorrectCommandProcessor(botMessageProperties);
  }

  @Bean("start")
  public StartCommandProcessor startCommandProcessor() {
    return new StartCommandProcessor(botMessageProperties);
  }

  @Bean("today")
  public TodayCommandProcessor todayCommandProcessor() {
    return new TodayCommandProcessor(botMessageProperties, taskConnector);
  }

  @Bean("tomorrow")
  public TomorrowCommandProcessor tomorrowCommandProcessor() {
    return new TomorrowCommandProcessor(botMessageProperties, taskConnector);
  }

  @Bean("failed")
  public TaskStateCommandProcessor failedCommandProcessor() {
    return new TaskStateCommandProcessor(botMessageProperties, taskConnector);
  }

  @Bean("upcoming")
  public TaskStateCommandProcessor upcomingCommandProcessor() {
    return failedCommandProcessor();
  }

  @Bean("completed")
  public TaskStateCommandProcessor completedCommandProcessor() {
    return failedCommandProcessor();
  }

  @Bean("in_process")
  public TaskStateCommandProcessor inProcessCommandProcessor() {
    return failedCommandProcessor();
  }
}
