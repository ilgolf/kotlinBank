package me.golf.kotlin.domain.member.dto.request

import me.golf.kotlin.domain.member.model.Birthday
import me.golf.kotlin.domain.member.model.ProfileImage
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class MemberApiUpdateRequestDto(

    @NotBlank(message = "필수 값입니다.")
    val name: String,

    @NotBlank(message = "필수 값입니다.")
    val nickname: String,

    @NotNull(message = "필수 값입니다.")
    val profileImage: ProfileImage,

    @NotNull(message = "필수 값입니다.")
    val birthday: Birthday
) {
    fun toService() = MemberUpdateRequestDto(name, nickname, profileImage, birthday)
}