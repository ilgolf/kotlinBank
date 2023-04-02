package me.golf.kotlin.domain.bank.nh.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.bank.policy.DefaultValuePolicy.DEFAULT_BALANCE

@JsonIgnoreProperties(ignoreUnknown = true)
data class BalanceNhResponseDto (

    @field:JsonProperty("Header")
    val commonHeader: NhCommonResponseHeader,

    @field:JsonProperty("RlpmAbamt")
    val balance: String = DEFAULT_BALANCE
) {

    companion object {
        fun createDefault(balance: String) = BalanceNhResponseDto(NhCommonResponseHeader(), balance)
    }
}

