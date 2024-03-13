package loadrover.api.io.domain.generator

import jakarta.persistence.*
import loadrover.api.io.domain.base.BaseEntity
import loadrover.api.io.domain.run.RunEntity
import java.time.LocalDateTime

@Entity
@Table(name ="generator")
class GeneratorEntity(

    @Column(nullable = false, length = 100)
    var hostAddress: String, // 호스트 주소

    @Column(nullable = true)
    var heartbeatLastAt: LocalDateTime? = null // healthCheck ?

): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @OneToMany(mappedBy = "generator", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var runList: MutableSet<RunEntity> = LinkedHashSet()

}