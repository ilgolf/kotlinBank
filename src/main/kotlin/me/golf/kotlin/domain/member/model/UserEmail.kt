package me.golf.kotlin.domain.member.model

import com.fasterxml.jackson.annotation.JsonValue
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class UserEmail(
    @Column(unique = true, nullable = false)
    @JsonValue
    var email: String) {

    fun getHost(): String {
        val index = email.indexOf("@")
        return email.substring(index + 1)
    }

    fun getId(): String {
        val index = email.indexOf("@")
        return email.substring(0, index)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEmail

        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        return email.hashCode()
    }
}
