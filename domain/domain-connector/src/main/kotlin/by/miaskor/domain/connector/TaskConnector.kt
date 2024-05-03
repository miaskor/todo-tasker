package by.miaskor.domain.connector

import by.miaskor.domain.model.task.CreateTaskRequest
import by.miaskor.domain.model.task.SearchTaskRequest
import by.miaskor.domain.model.task.TaskResponse
import feign.Headers
import feign.Param
import feign.RequestLine

@Headers(value = ["Content-type: application/json"])
interface TaskConnector {

  @RequestLine("POST /")
  fun create(task: CreateTaskRequest): TaskResponse

  @RequestLine("POST /search")
  fun getBy(searchTaskRequest: SearchTaskRequest): List<TaskResponse>

  @RequestLine("GET /client_id={client_id}")
  fun getAllByClientId(@Param("client_id") clientId: Int): List<TaskResponse>

  @RequestLine("PATCH /{id}")
  fun update(@Param("id") taskId: Long, task: CreateTaskRequest): TaskResponse

  @RequestLine("DELETE /{id}")
  fun delete(@Param("id") taskId: Long)
}
