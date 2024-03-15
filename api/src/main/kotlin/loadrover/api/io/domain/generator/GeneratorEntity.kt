package loadrover.api.io.domain.generator

import jakarta.persistence.*
import loadrover.api.io.domain.base.BaseEntity
import loadrover.api.io.domain.run.RunEntity
import loadrover.api.io.domain.task.TaskEntity
import java.time.LocalDateTime

@Entity
@Table(name ="generator")
class GeneratorEntity(

    @Column(nullable = false, length = 100)
    var hostAddress: String, // 호스트 주소

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    var type: Type,

    @ManyToOne
    var task: TaskEntity

): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

}

enum class Type {
    HOST_1, HOST_2, HOST_3
}