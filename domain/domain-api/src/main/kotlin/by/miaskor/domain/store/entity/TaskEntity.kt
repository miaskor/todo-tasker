package by.miaskor.domain.store.entity

import by.miaskor.domain.model.task.TaskState
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "task")
open class TaskEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  open val id: Long = -1,
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "client_id")
  open val client: ClientEntity = ClientEntity(),
  open val date: LocalDate = LocalDate.now(),
  open val taskName: String = "",
  @Enumerated(EnumType.STRING)
  open val taskState: TaskState = TaskState.UPCOMING,
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as TaskEntity

    if (id != other.id) return false
    if (date != other.date) return false
    if (taskName != other.taskName) return false
    if (taskState != other.taskState) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id.hashCode()
    result = 31 * result + date.hashCode()
    result = 31 * result + taskName.hashCode()
    result = 31 * result + taskState.hashCode()
    return result
  }

  override fun toString(): String {
    return "TaskEntity(id=$id, date=$date, taskName='$taskName', taskState=$taskState)"
  }
}
