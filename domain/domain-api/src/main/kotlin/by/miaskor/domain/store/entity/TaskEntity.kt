package by.miaskor.domain.store.entity

import by.miaskor.domain.model.task.TaskState
import java.time.LocalDate
import javax.persistence.Column
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
data class TaskEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = -1,
  //TODO(FIX THIS ASAP)
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "client_id", updatable = false, insertable = false)
  val client: ClientEntity? = null,
  @Column(name = "client_id")
  val clientId: Long? = null,
  val date: LocalDate = LocalDate.now(),
  val taskName: String = "",
  @Enumerated(EnumType.STRING)
  val taskState: TaskState = TaskState.UPCOMING,
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
