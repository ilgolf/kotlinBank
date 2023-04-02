package me.golf.kotlin.domain.bank.payment.dto

import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.bank.nh.dto.NhCommonResponseHeader

data class SimpleRefundResponseDto(

    @field:JsonProperty("Header")
    val header: NhCommonResponseHeader,
) {

    companion object {

        fun createDefault(): SimpleRefundResponseDto {
            return SimpleRefundResponseDto(NhCommonResponseHeader())
        }
    }
}