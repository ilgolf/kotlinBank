package me.golf.kotlin.domain.bank.nh.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.bank.policy.DefaultValuePolicy.DEFAULT_RESPONSE_MESSAGE

@JsonIgnoreProperties(ignoreUnknown = true)
data class NhCommonResponseHeader(

    @field:JsonProperty("Rsms")
    val responseMessage: String
) {

    constructor(): this(DEFAULT_RESPONSE_MESSAGE)
}