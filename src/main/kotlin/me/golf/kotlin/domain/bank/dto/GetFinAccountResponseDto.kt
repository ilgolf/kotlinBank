package me.golf.kotlin.domain.bank.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GetFinAccountResponseDto(

    @field:JsonProperty("FinAcno")
    val finAccount: String
)
