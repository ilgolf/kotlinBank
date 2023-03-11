package me.golf.kotlin.domain.bank.nh.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.bank.policy.DefaultValuePolicy.DEFAULT_NH_VALUE

@JsonIgnoreProperties(ignoreUnknown = true)
data class PublishRegisterNumberResponseDto(

    @field:JsonProperty("Header")
    val commonHeader: NhCommonResponseHeader,

    @field:JsonProperty("Rgno")
    val registerNumber: String = DEFAULT_NH_VALUE
) {

    companion object {
        fun createDefault(registerNumber: String) =
            PublishRegisterNumberResponseDto(NhCommonResponseHeader(), registerNumber)
    }
}
