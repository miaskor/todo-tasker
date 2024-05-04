package by.miaskor.domain.connector

import by.miaskor.domain.model.task.CreateTaskRequest
import by.miaskor.domain.model.task.SearchTaskRequest
import by.miaskor.domain.model.task.TaskResponse
import by.miaskor.domain.model.task.TaskState
import feign.Headers
import feign.Param
import feign.RequestLine
import feign.hystrix.FallbackFactory
import java.time.LocalDate
import java.util.*

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

  class TaskConnectorFallbackFactory : FallbackFactory<TaskConnector> {
    override fun create(p0: Throwable?): TaskConnector {
      return object : TaskConnector {
        override fun create(task: CreateTaskRequest): TaskResponse {
          return TaskResponse("error", TaskState.FAILED, LocalDate.MIN)
        }

        override fun getBy(searchTaskRequest: SearchTaskRequest): List<TaskResponse> {
          return Collections.singletonList(TaskResponse("error", TaskState.FAILED, LocalDate.MIN))
        }

        override fun getAllByClientId(clientId: Int): List<TaskResponse> {
          return Collections.singletonList(TaskResponse("error", TaskState.FAILED, LocalDate.MIN))
        }

        override fun update(taskId: Long, task: CreateTaskRequest): TaskResponse {
          return TaskResponse("error", TaskState.FAILED, LocalDate.MIN)
        }

        override fun delete(taskId: Long) {
        }
      }
    }
  }
}
