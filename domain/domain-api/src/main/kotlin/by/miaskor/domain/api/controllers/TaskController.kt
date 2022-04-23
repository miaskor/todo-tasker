package by.miaskor.domain.api.controllers

import by.miaskor.domain.api.exceptions.BadRequestException
import by.miaskor.domain.api.exceptions.NotFoundException
import by.miaskor.domain.dto.TaskDtoRequest
import by.miaskor.domain.dto.TaskDtoResponse
import by.miaskor.domain.dto.TaskState
import by.miaskor.domain.factories.TaskDtoResponseFactory
import by.miaskor.domain.store.entities.TaskEntity
import by.miaskor.domain.store.repositories.ClientRepository
import by.miaskor.domain.store.repositories.TaskRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.*

@RestController
@RequestMapping("/api")
open class TaskController(
  private val taskRepository: TaskRepository,
  private val clientRepository: ClientRepository,
  private val taskDtoResponseFactory: TaskDtoResponseFactory
) {

  @GetMapping(GET_TASKS_BY_BOT_ID_AND_STATE)
  fun getAllByBotIdAndState(@PathVariable botId: Long, @PathVariable state: String): ResponseEntity<List<TaskDtoResponse>> {
    val clientEntity = clientRepository.findByBotId(botId)
      .orElseThrow { NotFoundException("Client with bot id $botId not exists or not used") }
    val listTasks = taskRepository.findByClientIdAndTaskState(
      clientEntity.id,
      TaskState.valueOf(state.uppercase(Locale.getDefault()))
    )
    return ResponseEntity.ok(
      listTasks.map { taskDtoResponseFactory.makeTaskDtoResponse(it) }.toList()
    )
  }

  @GetMapping(GET_TASKS_ON_CURRENT_DAY_BY_BOT_ID)
  fun getTasksOnCurrentDayByBotId(@PathVariable botId: Long): ResponseEntity<List<TaskDtoResponse>> {
    val clientEntity = clientRepository.findByBotId(botId)
      .orElseThrow { NotFoundException("Client with bot id $botId not exists or not used") }
    val listTasks = taskRepository.findByDateAndClientId(LocalDate.now(), clientEntity.id)
    return ResponseEntity.ok(
      listTasks.map { taskDtoResponseFactory.makeTaskDtoResponse(it) }.toList()
    )
  }

  @GetMapping(GET_TASKS_ON__DAY_BY_DATE_AND_BOT_ID)
  fun getAllByBotIdAndDate(@PathVariable botId: Long, @PathVariable date: String):
          ResponseEntity<List<TaskDtoResponse>> {
    val parseDate: LocalDate
    try {
      parseDate = LocalDate.parse(date)
    } catch (ex: DateTimeParseException) {
      throw BadRequestException("DateTo = $date can't be parsed, bad format")
    }
    val clientEntity = clientRepository.findByBotId(botId)
      .orElseThrow { NotFoundException("Client with bot id $botId not exists or not used") }
    val listTasks = taskRepository.findByDateAndClientId(parseDate, clientEntity.id)
    return ResponseEntity.ok(
      listTasks.map { taskDtoResponseFactory.makeTaskDtoResponse(it) }.toList()
    )
  }

  @GetMapping(GET_TASKS_ON_TOMORROW_BY_BOT_ID)
  fun getTasksOnTomorrowByBotId(@PathVariable botId: Long): ResponseEntity<List<TaskDtoResponse>> {
    val clientEntity = clientRepository.findByBotId(botId)
      .orElseThrow { NotFoundException("Client with bot id $botId not exists or not used") }
    val listTasks = taskRepository.findByDateAndClientId(LocalDate.now().plusDays(1), clientEntity.id)
    return ResponseEntity.ok(
      listTasks.map { taskDtoResponseFactory.makeTaskDtoResponse(it) }.toList()
    )
  }

  @PostMapping(CREATE_TASK)
  fun create(@RequestBody taskDtoRequest: TaskDtoRequest): ResponseEntity<TaskDtoResponse> {
    if (taskDtoRequest.taskName.trim().isEmpty()) {
      throw BadRequestException("Task name can't be empty")
    }
    clientRepository.findById(taskDtoRequest.clientId)
      .orElseThrow { NotFoundException("Client with id = ${taskDtoRequest.clientId} doesn't exists") }
    val taskEntity = taskRepository.saveAndFlush(
      TaskEntity(
        clientId = taskDtoRequest.clientId,
        taskName = taskDtoRequest.taskName,
        date = taskDtoRequest.date,
        taskState = taskDtoRequest.taskState
      )
    )
    return ResponseEntity.ok(taskDtoResponseFactory.makeTaskDtoResponse(taskEntity))
  }

  @PatchMapping(UPDATE_TASK)
  fun update(@PathVariable("id") taskId: Int, @RequestBody taskDtoRequest: TaskDtoRequest)
      : ResponseEntity<TaskDtoResponse> {
    if (taskDtoRequest.taskName.trim().isEmpty()) {
      throw BadRequestException("Task name can't be empty")
    }
    clientRepository.findById(taskDtoRequest.clientId)
      .orElseThrow { NotFoundException("Client with id = ${taskDtoRequest.clientId} doesn't exists") }
    taskRepository.findByIdAndClientId(taskId, taskDtoRequest.clientId)
      .orElseThrow { NotFoundException("Task with id = $taskId doesn't exists") }
    val taskEntity = taskRepository.saveAndFlush(
      TaskEntity(
        id = taskId,
        clientId = taskDtoRequest.clientId,
        taskName = taskDtoRequest.taskName,
        date = taskDtoRequest.date,
        taskState = taskDtoRequest.taskState
      )
    )
    return ResponseEntity.ok(taskDtoResponseFactory.makeTaskDtoResponse(taskEntity))
  }

  @GetMapping(GET_ALL_BY_CLIENT_ID_IN_RANGE)
  fun getAllByClientIdAndDateBetween(
    @RequestParam("date_from") dateFrom: String,
    @RequestParam("date_to") dateTo: String,
    @RequestParam("client_id") clientId: Int
  ): ResponseEntity<Map<String, List<TaskDtoResponse>>> {
    val parseDateFrom: LocalDate
    val parseDateTo: LocalDate
    try {
      parseDateFrom = LocalDate.parse(dateFrom)
      try {
        parseDateTo = LocalDate.parse(dateTo)
      } catch (ex: DateTimeParseException) {
        throw BadRequestException("DateTo = $dateTo can't be parsed, bad format")
      }
      if (dateFrom > dateTo) {
        throw BadRequestException("DateFrom = $dateFrom is bigger than DateTo = $dateTo")
      }
    } catch (ex: DateTimeParseException) {
      throw BadRequestException("DateFrom = $dateFrom can't be parsed, bad format")
    }
    val taskList =
      taskRepository.findByDateBetweenAndClientIdOrderByDate(parseDateFrom, parseDateTo, clientId)
    val mapTasks = taskList.groupBy { it.date }.map { it1 ->
      Pair(it1.key.toString(), it1.value.map {
        taskDtoResponseFactory
          .makeTaskDtoResponse(it)
      }.toList())
    }.toMap()
    return ResponseEntity.ok(mapTasks)
  }

  @GetMapping(GET_ALL_BY_CLIENT_ID_AND_DATE)
  fun getAllByClientIdAndDate(@RequestParam("client_id") clientId: Int, @RequestParam date: String):
      ResponseEntity<List<TaskDtoResponse>> {
    val parseDate: LocalDate
    try {
      parseDate = LocalDate.parse(date)
    } catch (ex: DateTimeParseException) {
      throw BadRequestException("DateTo = $date can't be parsed, bad format")
    }
    clientRepository.findById(clientId)
      .orElseThrow { NotFoundException("Client with id = $clientId doesn't exists") }
    val listTaskEntities = taskRepository.findByDateAndClientId(parseDate, clientId)
    return ResponseEntity.ok(
      listTaskEntities.map { taskDtoResponseFactory.makeTaskDtoResponse(it) }.toList()
    )
  }

  @GetMapping(GET_ALL_BY_CLIENT_ID)
  fun getAllByClientId(@RequestParam("client_id") clientId: Int): ResponseEntity<List<TaskDtoResponse>> {
    clientRepository.findById(clientId)
      .orElseThrow { NotFoundException("Client with id = $clientId doesn't exists") }
    val listTaskEntities = taskRepository.findByClientId(clientId)
    return ResponseEntity.ok(
      listTaskEntities.map { taskDtoResponseFactory.makeTaskDtoResponse(it) }.toList()
    )
  }

  @DeleteMapping(DELETE_TASK)
  fun delete(@PathVariable("id") taskId: Int) {
    taskRepository.findById(taskId)
      .orElseThrow { NotFoundException("Task with id $taskId doesn't not exists") }
    taskRepository.deleteById(taskId)
  }

  companion object {
    private const val CREATE_TASK = "/tasks"
    private const val UPDATE_TASK = "/tasks/{id}"
    private const val DELETE_TASK = "/tasks/{id}"
    private const val GET_ALL_BY_CLIENT_ID_IN_RANGE = "/tasks/range"
    private const val GET_ALL_BY_CLIENT_ID_AND_DATE = "/tasks/date"
    private const val GET_ALL_BY_CLIENT_ID = "/tasks/all"
    private const val GET_TASKS_ON_CURRENT_DAY_BY_BOT_ID = "/tasks/currentDay/{botId}"
    private const val GET_TASKS_ON_TOMORROW_BY_BOT_ID = "/tasks/tomorrow/{botId}"
    private const val GET_TASKS_ON__DAY_BY_DATE_AND_BOT_ID = "/tasks/day/{botId}/{date}"
    private const val GET_TASKS_BY_BOT_ID_AND_STATE = "/tasks/state/{botId}/{state}"
  }
}
