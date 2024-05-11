package by.miaskor.domain.connector

import by.miaskor.domain.model.Result
import by.miaskor.domain.model.task.CreateTaskRequest
import by.miaskor.domain.model.task.SearchTaskRequest
import by.miaskor.domain.model.task.TaskResponse
import feign.Headers
import feign.Param
import feign.RequestLine
import feign.hystrix.FallbackFactory
import java.util.*

@Headers(value = ["Content-type: application/json"])
interface TaskConnector {

  @RequestLine("POST /")
  fun create(task: CreateTaskRequest): Result<TaskResponse>

  @RequestLine("POST /search")
  fun getBy(searchTaskRequest: SearchTaskRequest): List<Result<TaskResponse>>

  @RequestLine("GET /client_id={client_id}")
  fun getAllByClientId(@Param("client_id") clientId: Int): List<Result<TaskResponse>>

  @RequestLine("PATCH /{id}")
  fun update(@Param("id") taskId: Long, task: CreateTaskRequest): Result<TaskResponse>

  @RequestLine("DELETE /{id}")
  fun delete(@Param("id") taskId: Long)

  class TaskConnectorFallbackFactory : FallbackFactory<TaskConnector> {
    override fun create(p0: Throwable?): TaskConnector {
      return object : TaskConnector {
        override fun create(task: CreateTaskRequest): Result<TaskResponse> {
          return Result()
        }

        override fun getBy(searchTaskRequest: SearchTaskRequest): List<Result<TaskResponse>> {
          return Collections.singletonList(Result())
        }

        override fun getAllByClientId(clientId: Int): List<Result<TaskResponse>> {
          return Collections.singletonList(Result())
        }

        override fun update(taskId: Long, task: CreateTaskRequest): Result<TaskResponse> {
          return Result()
        }

        override fun delete(taskId: Long) {
        }
      }
    }
  }
}
