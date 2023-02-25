package me.golf.kotlin.domain.member.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Birthday (
    @Column(name = "birth", columnDefinition = "date")
    val value: LocalDate
) {

    fun getAge(): Int {
        return LocalDate.now().year - this.value.year
    }

    fun getStringValue(): String{
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("YYYYMMDD")
        return this.value.format(formatter)
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
