package me.golf.kotlin.domain.member.model

import com.fasterxml.jackson.annotation.JsonValue
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class ProfileImage (
    @Column(length = 100)
    @JsonValue
    var value: String?
    ) {

    fun validateImagePath(profileImageUrl: String): Boolean {
        val regex = "([A-Za-z]*:\\/\\/)?\\S*".toRegex()
        return regex.containsMatchIn(profileImageUrl)
    }
}