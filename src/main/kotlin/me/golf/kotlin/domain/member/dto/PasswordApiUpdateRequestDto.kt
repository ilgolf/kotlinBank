package me.golf.kotlin.domain.member.dto

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class PasswordApiUpdateRequestDto(

    @field:NotBlank(message = "필수 값입니다.")
    @JsonProperty("password")
    var password: String,

    @field:NotBlank(message = "필수 값입니다.")
    @JsonProperty("passwordConfirm")
    var passwordConfirm: String
) {

    fun validPassword() = this.password == this.passwordConfirm
}
