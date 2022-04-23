package by.miaskor.domain.connector

import by.miaskor.domain.dto.TaskDtoRequest
import by.miaskor.domain.dto.TaskDtoResponse
import feign.Headers
import feign.Param
import feign.RequestLine

@Headers(value = ["Content-type: application/json"])
interface TaskConnector {

  @RequestLine("POST /")
  fun create(task: TaskDtoRequest): TaskDtoResponse

  @RequestLine("GET /range?date_from={date_from}&date_to={date_to}&client_id={client_id}")
  fun getAllByClientIdAndDateBetween(
    @Param("date_from") dateFrom: String,
    @Param("date_to") dateTo: String,
    @Param("client_id") clientId: Int
  ): Map<String, List<TaskDtoResponse>>

  @RequestLine("GET /date?date={date}&client_id={client_id}")
  fun getAllByClientIdAndDate(
    @Param("date") date: String,
    @Param("client_id") clientId: Int
  ): List<TaskDtoResponse>

  @RequestLine("GET /all?client_id={client_id}")
  fun getAllByClientId(@Param("client_id") clientId: Int): List<TaskDtoResponse>

  @RequestLine("PATCH /{id}")
  fun update(@Param("id") taskId: Int, task: TaskDtoRequest): TaskDtoResponse

  @RequestLine("DELETE /{id}")
  fun delete(@Param("id") taskId: Int)

  @RequestLine("GET /currentDay/{bot_id}")
  fun getTasksOnCurrentDayByBotId(@Param("bot_id") botId: Long): List<TaskDtoResponse>

  @RequestLine("GET /tomorrow/{bot_id}")
  fun getTasksOnTomorrowByBotId(@Param("bot_id") botId: Long): List<TaskDtoResponse>

  @RequestLine("GET /day/{bot_id}/{date}")
  fun getAllByBotIdAndDate(@Param("bot_id") botId: Long, @Param date: String): List<TaskDtoResponse>

  @RequestLine("GET /state/{bot_id}/{state}")
  fun getAllByBotIdAndState(@Param("bot_id") botId: Long, @Param state: String): List<TaskDtoResponse>
}
