package me.golf.kotlin.domain.member.dto.request

import me.golf.kotlin.domain.member.model.*
import java.time.LocalDate

data class MemberSaveRequestDto(
    var email: String,
    var password: String,
    var name: String,
    var nickname: String,
    var birth: LocalDate,
    var profileImageUrl: String,
    var phoneNumber: String
) {

    fun toMemberEntity(): Member {
        return Member(
            email = UserEmail(email),
            password = password,
            name = name,
            nickname = nickname,
            birth = Birthday(birth),
            profileImage = ProfileImage(profileImageUrl),
            phoneNumber = phoneNumber
        )
    }
}
