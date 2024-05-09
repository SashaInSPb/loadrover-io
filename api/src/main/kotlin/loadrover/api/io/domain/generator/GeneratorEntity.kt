package loadrover.api.io.domain.generator

import jakarta.persistence.*
import loadrover.api.io.domain.base.BaseEntity
import loadrover.api.io.domain.task.TaskEntity

@Entity
@Table(name ="generator")
class GeneratorEntity(

    @Column(nullable = false, length = 100)
    var hostAddress: String, // 호스트 주소

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    var type: HostType,

    @ManyToOne
    var task: TaskEntity

): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

}

enum class HostType(
    val value: String
) {
    HOST_1("10.88.12.189"),
    HOST_2("10.88.12.190"),
    HOST_3("10.88.12.138")
}