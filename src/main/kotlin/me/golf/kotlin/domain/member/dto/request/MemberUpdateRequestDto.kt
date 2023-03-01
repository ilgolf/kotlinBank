package me.golf.kotlin.domain.member.dto.request

import me.golf.kotlin.domain.member.model.Birthday
import me.golf.kotlin.domain.member.model.ProfileImage

data class MemberUpdateRequestDto(
    var name: String,
    var nickname: String,
    var profileImage: ProfileImage,
    var birthday: Birthday
)
