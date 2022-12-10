package me.golf.kotlin.domain.member.sms.dto

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class SendAuthNumberApiRequestDto(

    @field:NotBlank(message = "필수 값입니다.")
    @field:Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "휴대폰 형식에서 벗어납니다.")
    @JsonProperty("phoneNumber")
    val phoneNumber: String
)