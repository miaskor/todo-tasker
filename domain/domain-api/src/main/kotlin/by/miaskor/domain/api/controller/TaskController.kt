package by.miaskor.domain.api.controller

import by.miaskor.domain.factory.TaskDtoResponseFactory
import by.miaskor.domain.model.task.CreateTaskRequest
import by.miaskor.domain.model.task.SearchTaskRequest
import by.miaskor.domain.model.task.TaskResponse
import by.miaskor.domain.model.task.UpdateTaskRequest
import by.miaskor.domain.service.TaskService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/tasks")
open class TaskController(
  private val taskDtoResponseFactory: TaskDtoResponseFactory,
  private val taskService: TaskService,
) {

  @PostMapping("/search")
  fun search(@RequestBody searchTaskRequest: SearchTaskRequest): List<TaskResponse> {
    return taskService.search(searchTaskRequest)
      .map(taskDtoResponseFactory::makeTaskDtoResponse)
      .toList()
  }

  @PostMapping
  @ResponseStatus(CREATED)
  fun create(@RequestBody createTaskRequest: CreateTaskRequest): TaskResponse {
    return taskService.create(createTaskRequest)
      .let(taskDtoResponseFactory::makeTaskDtoResponse)
  }

  @PatchMapping("/{id}")
  fun update(@PathVariable("id") id: Long, @RequestBody updateTaskRequest: UpdateTaskRequest): TaskResponse {
    return taskService.updateById(id, updateTaskRequest)
      .let(taskDtoResponseFactory::makeTaskDtoResponse)
  }

  @DeleteMapping("/{id}")
  fun delete(@PathVariable("id") id: Long) {
    taskService.deleteById(id)
  }

  @GetMapping
  fun getAllByClientId(@RequestParam("client_id") clientId: Long, pageable: Pageable): List<TaskResponse> {
    return taskService.getAllByClientIdWithPagination(clientId, pageable)
      .map(taskDtoResponseFactory::makeTaskDtoResponse)
      .toList()
  }
}
