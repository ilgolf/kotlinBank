package me.golf.kotlin.domain.member.util

import me.golf.kotlin.domain.member.model.*
import java.time.LocalDate

object GivenMember {
    val email = UserEmail("ilgolc@naver.com")
    const val password = "1234"
    const val name = "노경태"
    const val nickname = "nokt"
    val birth = Birthday(LocalDate.of(1996, 10, 25))
    const val phoneNumber = "010-1234-5678"
    val profileImage = ProfileImage("/Users/test/config/image.png")


    fun toMember() =
        Member(
            email = email,
            password = password,
            name = name,
            nickname = nickname,
            birth = birth,
            roleType = RoleType.USER,
            profileImage = profileImage,
            phoneNumber = phoneNumber
        )
}