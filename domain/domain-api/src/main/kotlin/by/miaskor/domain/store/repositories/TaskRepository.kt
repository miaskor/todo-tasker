package by.miaskor.domain.store.repositories

import by.miaskor.domain.dto.TaskState
import by.miaskor.domain.store.entities.TaskEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.*

interface TaskRepository : JpaRepository<TaskEntity, Int> {
  fun findByIdAndClientId(id: Int, clientId: Int): Optional<TaskEntity>
  fun findByDateBetweenAndClientIdOrderByDate(dateFrom: LocalDate, dateTo: LocalDate, clientId: Int): List<TaskEntity>
  fun findByDateAndClientId(date: LocalDate, clientId: Int): List<TaskEntity>
  fun findByClientId(clientId: Int): List<TaskEntity>
  fun findByClientIdAndTaskState(clientId: Int, taskState: TaskState): List<TaskEntity>
}
