package loadrover.api.io.domain.generator

import jakarta.persistence.*
import loadrover.api.io.domain.base.BaseEntity

@Entity
@Table(name ="host")
class HostEntity(

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var type: Type,

    @Column(nullable = false, length = 30)
    var ipAddress: String

): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

}

enum class Type {
    HOST_1, HOST_2, HOST_3
}

