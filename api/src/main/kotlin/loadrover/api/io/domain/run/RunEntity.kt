package loadrover.api.io.domain.run

import jakarta.persistence.*
import loadrover.api.io.domain.base.BaseEntity
import loadrover.api.io.domain.generator.GeneratorEntity
import loadrover.api.io.domain.task.TaskEntity

@Entity
@Table(name = "run")
class RunEntity(

    @Column(nullable = true)
    var runOrder: Int? = 1, // 실행 순서

//    @Column(nullable = true)
//    var reportPath: String? = null, // 보고서 링크

    @Column(nullable = true)
    var hostIp: String? = null, // generator IP

    @ManyToOne
    var task: TaskEntity

): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

}