package me.golf.kotlin.domain.bank.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.bank.nh.dto.NhCommonResponseHeader

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetFinAccountResponseDto(

    @field:JsonProperty("Header")
    val commonHeader: NhCommonResponseHeader,

    @field:JsonProperty("FinAcno")
    val finAccount: String
) {

    companion object {
        fun createDefault(finAccount: String) = GetFinAccountResponseDto(NhCommonResponseHeader(), finAccount)
    }
}
