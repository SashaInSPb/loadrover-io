package loadrover.api.io.domain.base

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.time.LocalDateTime

@MappedSuperclass
@EnableJpaAuditing
@EntityListeners(value = [AuditingEntityListener::class])
class BaseEntity {

    @CreatedDate
    @Column(name = "createdDateTime")
    @ColumnDefault("now()")
    var createdDateTime: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(name = "updatedDateTime")
    @ColumnDefault("null")
    var updatedDateTime: LocalDateTime? = null
}