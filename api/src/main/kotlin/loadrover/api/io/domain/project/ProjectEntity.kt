package loadrover.api.io.domain.project

import jakarta.persistence.*
import loadrover.api.io.domain.base.BaseEntity
import loadrover.api.io.domain.task.TaskEntity
import java.time.LocalDateTime

@Entity
@Table(name = "project")
class ProjectEntity(

    @Column(nullable = false, length = 100)
    var title: String, // 프로젝트명

    @Column(nullable = true)
    var clientName: String? = null,

    @Column(nullable = true)
    var startDateTime: LocalDateTime? = null, // 계약 시작일

    @Column(nullable = true)
    var endDateTime: LocalDateTime? = null, // 계약 종료일

): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @OneToMany(mappedBy = "project", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var taskList: MutableSet<TaskEntity> = LinkedHashSet()

}