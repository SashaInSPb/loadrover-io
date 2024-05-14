package loadrover.api.io.domain.task

import jakarta.persistence.*
import loadrover.api.io.domain.base.BaseEntity
import loadrover.api.io.domain.generator.GeneratorEntity
import loadrover.api.io.domain.project.ProjectEntity
import loadrover.api.io.domain.run.RunEntity

@Entity
@Table(name = "task")
class TaskEntity(

    @Column(nullable = false, length = 100)
    var title: String, // 작업명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: TaskStatus,

    @Column(nullable = true, length = 100)
    var uploadFileName: String? = null, // 파일명

    @Column(nullable = true, length = 600)
    var description: String? = null, // 작업 설명

    // FIXME: RunEntity로 옮길 것
    @Column(nullable = true)
    var reportPath: String? = null, // 보고서 링크

    @Column(nullable = true)
    var runCount: Int? = 0, // 실행 횟수

    @ManyToOne
    var project: ProjectEntity

): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @OneToMany(mappedBy = "task", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var runList: MutableSet<RunEntity> = LinkedHashSet()

    @OneToMany(mappedBy = "task", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var generatorList: MutableSet<GeneratorEntity> = LinkedHashSet()

}

enum class TaskStatus {
    NEW, PROGRESS, COMPLETE
}