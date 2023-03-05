package me.golf.kotlin.domain.bank.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.bank.nh.dto.NhCommonResponseHeader
import me.golf.kotlin.domain.bank.policy.DefaultValuePolicy.DEFAULT_NH_VALUE

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetFinAccountResponseDto(

    @field:JsonProperty("Header")
    val commonHeader: NhCommonResponseHeader,

    @field:JsonProperty("FinAcno")
    val finAccount: String = DEFAULT_NH_VALUE
) {

    companion object {
        fun createDefault(finAccount: String) = GetFinAccountResponseDto(NhCommonResponseHeader(), finAccount)
    }
}
