package me.golf.kotlin.global.common

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
open class BaseEntity(

    @CreatedBy
    @Column(updatable = false)
    open var createdBy: Long = 0L,

    @LastModifiedBy
    open var lastModifiedBy: Long = 0L
): BaseTimeEntity()