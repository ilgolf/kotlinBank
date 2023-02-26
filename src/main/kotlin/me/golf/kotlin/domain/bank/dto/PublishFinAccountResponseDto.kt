package me.golf.kotlin.domain.bank.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PublishFinAccountResponseDto(

    @field:JsonProperty("Rgno")
    val registerNumber: String
)
