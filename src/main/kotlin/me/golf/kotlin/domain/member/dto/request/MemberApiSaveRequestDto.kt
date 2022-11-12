package me.golf.kotlin.domain.member.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class MemberApiSaveRequestDto (

    @field:NotBlank(message = "필수 값입니다.")
    @JsonProperty("email")
    var email: String,

    @field:NotBlank(message = "필수 값입니다.")
    @JsonProperty("password")
    var password: String,

    @field:NotBlank(message = "필수 값입니다.")
    @JsonProperty("passwordConfirm")
    var passwordConfirm: String,

    @field:NotBlank(message = "필수 값입니다.")
    @JsonProperty("name")
    var name: String,

    @field:NotBlank(message = "필수 값입니다.")
    @JsonProperty("nickname")
    var nickname: String,

    @field:NotNull(message = "필수 값입니다.")
    @JsonProperty("birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var birth: LocalDate,

    @field:NotBlank(message = "필수 값입니다.")
    @JsonProperty("profileImageUrl")
    var profileImageUrl: String,

    @field:NotBlank(message = "필수 값입니다.")
    @JsonProperty("phoneNumber")
    var phoneNumber: String,

    @field:NotNull(message = "필수 값입니다.")
    @JsonProperty("isPhoneConfirm")
    var isPhoneConfirm: Boolean
) {

    fun toServiceDto() = MemberSaveRequestDto(email, password, name, nickname, birth, profileImageUrl, phoneNumber)
}
