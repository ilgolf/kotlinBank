package me.golf.kotlin.domain.bank.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.bank.nh.dto.NhCommonResponseHeader

@JsonIgnoreProperties(ignoreUnknown = true)
data class BalanceNhResponseDto (

    @field:JsonProperty("Header")
    val commonHeader: NhCommonResponseHeader,

    @field:JsonProperty("RlpmAbamt")
    val balance: String
) {

    companion object {
        fun createDefault(balance: String) = BalanceNhResponseDto(NhCommonResponseHeader(), balance)
    }
}

