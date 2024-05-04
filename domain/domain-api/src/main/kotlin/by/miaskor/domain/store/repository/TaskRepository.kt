package by.miaskor.domain.store.repository

import by.miaskor.domain.store.entity.TaskEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {
}
