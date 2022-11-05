package me.golf.kotlin.domain.member.dto.response

import com.querydsl.core.annotations.QueryProjection
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.ProfileImage
import me.golf.kotlin.domain.member.model.UserEmail

data class MemberApiShortResponseDto

@QueryProjection
constructor(
    val email: UserEmail,
    val name: String,
    val profileImage: ProfileImage
) {
    companion object {
        fun of(member: Member): MemberApiShortResponseDto {
           return MemberApiShortResponseDto(member.email, member.name, member.profileImage)
        }
    }
}
