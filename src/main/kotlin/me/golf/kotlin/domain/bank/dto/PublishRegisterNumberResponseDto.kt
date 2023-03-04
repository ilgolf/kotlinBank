package me.golf.kotlin.domain.bank.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.bank.nh.dto.NhCommonResponseHeader
import me.golf.kotlin.domain.bank.policy.DefaultValuePolicy
import me.golf.kotlin.domain.bank.policy.DefaultValuePolicy.DEFAULT_REGISTER_NUMBER

@JsonIgnoreProperties(ignoreUnknown = true)
data class PublishRegisterNumberResponseDto(

    @field:JsonProperty("Header")
    val commonHeader: NhCommonResponseHeader,

    @field:JsonProperty("Rgno")
    val registerNumber: String = DEFAULT_REGISTER_NUMBER
) {

    companion object {
        fun createDefault(registerNumber: String) =
            PublishRegisterNumberResponseDto(NhCommonResponseHeader(), registerNumber)
    }
}
