package me.golf.kotlin.domain.bank.dto

import me.golf.kotlin.domain.bank.nh.utils.NhHeaderValueUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class BalanceRequestDto(
    val commonHeader: NhCommonHeader,
    val finAccount: String
) {

    companion object {
        fun of(finAccount: String): BalanceRequestDto {
            val nowDateTimeParse = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd hhmmss"))
                .split(" ")

            return BalanceRequestDto(
                NhCommonHeader(
                    NhHeaderValueUtils.GET_API_NAME_VALUE,
                    nowDateTimeParse[0],
                    nowDateTimeParse[1],
                    NhHeaderValueUtils.AGENCY_CODE_VALUE,
                    NhHeaderValueUtils.FINTECH_NUMBER_VALUE,
                    NhHeaderValueUtils.SERVICE_CODE_VALUE,
                    NhHeaderValueUtils.createAgencyDealCode().toString(),
                    NhHeaderValueUtils.ACCESS_TOKEN_VALUE
                ),
                finAccount
            )
        }
    }
}