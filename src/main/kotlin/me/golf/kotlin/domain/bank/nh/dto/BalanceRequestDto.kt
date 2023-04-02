package me.golf.kotlin.domain.bank.nh.dto

import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.bank.nh.utils.NhHeaderValueUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class BalanceRequestDto(

    @field:JsonProperty("Header")
    val commonHeader: NhCommonRequestHeader,

    @field:JsonProperty("FinAcno")
    val finAccount: String
) {

    companion object {
        fun of(finAccount: String): BalanceRequestDto {
            val nowDateTimeParse = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd hhmmss"))
                .split(" ")

            return BalanceRequestDto(
                NhCommonRequestHeader(
                    NhHeaderValueUtils.BALANCE_API_NAME_VALUE,
                    nowDateTimeParse[0],
                    nowDateTimeParse[1],
                    NhHeaderValueUtils.AGENCY_CODE_VALUE,
                    NhHeaderValueUtils.FINTECH_NUMBER_VALUE,
                    NhHeaderValueUtils.BALANCE_SERVICE_CODE_VALUE,
                    NhHeaderValueUtils.createAgencyDealCode().toString(),
                    NhHeaderValueUtils.ACCESS_TOKEN_VALUE
                ),
                finAccount
            )
        }
    }
}