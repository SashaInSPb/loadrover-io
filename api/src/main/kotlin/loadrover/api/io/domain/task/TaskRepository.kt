package loadrover.api.io.domain.task

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository: JpaRepository<TaskEntity, Long> {
    @EntityGraph(attributePaths = ["runList"])
    fun findByProjectId(id: Long): MutableList<TaskEntity>
}