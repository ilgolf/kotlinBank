package me.golf.kotlin.domain.member.dto.response

import me.golf.kotlin.domain.member.model.Birthday
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.ProfileImage
import me.golf.kotlin.domain.member.model.UserEmail

data class MemberApiDetailDto(
    var email: UserEmail,
    var name: String,
    var nickname: String,
    var birth: Birthday,
    var profileImage: ProfileImage,
    var phoneNumber: String
) {

    companion object {
        fun of(member: Member): MemberApiDetailDto {
            return MemberApiDetailDto(member.email, member.name, member.nickname,
                member.birth, member.profileImage, member.phoneNumber)
        }
    }
}
