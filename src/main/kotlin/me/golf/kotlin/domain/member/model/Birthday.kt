package me.golf.kotlin.domain.member.model

import com.fasterxml.jackson.annotation.JsonValue
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Birthday (
    @Column(name = "birth", columnDefinition = "datetime")
    val value: LocalDate
) {

    fun getAge(): Int {
        return LocalDate.now().year - this.value.year
    }

    fun validateExceedNowYear(birth: LocalDate): Boolean {
        return birth > LocalDate.now()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Birthday

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}
