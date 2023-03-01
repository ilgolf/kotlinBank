package me.golf.kotlin.global.common

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseTimeEntity (
    @CreatedDate
    @Column(name = "created_at", updatable = false, columnDefinition = "datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    open var createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    open var lastModifiedDate: LocalDateTime = LocalDateTime.now()
)