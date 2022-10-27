package me.golf.kotlin.global.common

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.MappedSuperclass

@MappedSuperclass
open class BaseTimeEntity (
    @CreatedDate
    private var createdAt: LocalDateTime? = LocalDateTime.now(),

    @LastModifiedDate
    private var lastModifiedDate: LocalDateTime? = LocalDateTime.now()
)