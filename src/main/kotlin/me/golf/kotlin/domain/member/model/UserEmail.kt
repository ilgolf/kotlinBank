package me.golf.kotlin.domain.member.model

import com.fasterxml.jackson.annotation.JsonValue
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class UserEmail(
    @Column(name = "email", unique = true, nullable = false)
    @JsonValue
    val value: String): Serializable {

    fun getId(): String {
        val index = value.indexOf("@")
        return value.substring(0, index)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEmail

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
