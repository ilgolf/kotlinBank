package me.golf.kotlin.domain.member.dto

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class PasswordApiFindPasswordRequestDto(

    @field:NotNull(message = "필수 값입니다.")
    @JsonProperty("validPhone")
    val validPhone: Boolean,

    @field:NotBlank(message = "필수 값입니다.")
    @JsonProperty("phoneNumber")
    val phoneNumber: String
)
