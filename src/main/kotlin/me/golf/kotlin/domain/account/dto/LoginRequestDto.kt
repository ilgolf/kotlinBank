package me.golf.kotlin.domain.account.dto

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class LoginRequestDto (

    @field:NotBlank(message = "필수 값입니다.")
    @JsonProperty("email")
    val email: String,

    @field:NotBlank(message = "필수 값입니다.")
    @JsonProperty("password")
    val password: String
)
