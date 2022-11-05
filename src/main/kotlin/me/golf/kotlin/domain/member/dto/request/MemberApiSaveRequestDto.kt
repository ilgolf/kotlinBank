package me.golf.kotlin.domain.member.dto.request

import java.time.LocalDate
import javax.validation.constraints.NotBlank

data class MemberApiSaveRequestDto (

    @NotBlank(message = "필수 값입니다.")
    private var email: String,

    @NotBlank(message = "필수 값입니다.")
    private var password: String,

    @NotBlank(message = "필수 값입니다.")
    private var passwordConfirm: String,

    @NotBlank(message = "필수 값입니다.")
    private var name: String,

    @NotBlank(message = "필수 값입니다.")
    private var nickname: String,

    @NotBlank(message = "필수 값입니다.")
    private var birth: LocalDate,

    @NotBlank(message = "필수 값입니다.")
    private var profileImageUrl: String,

    @NotBlank(message = "필수 값입니다.")
    private var phoneNumber: String
) {

    fun toServiceDto(): MemberSaveRequestDto {
        return MemberSaveRequestDto(email, password, name, nickname, birth, profileImageUrl, phoneNumber)
    }
}

