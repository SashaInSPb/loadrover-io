package loadrover.api.io.domain.generator

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GeneratorRepository: JpaRepository<GeneratorEntity, Long> {

    @EntityGraph(attributePaths = ["task"])
    fun findAllByTaskId(id: Long): MutableList<GeneratorEntity>?
}