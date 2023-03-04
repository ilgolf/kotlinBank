package me.golf.kotlin.domain.bank.dto

import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.bank.nh.dto.NhCommonRequestHeader
import me.golf.kotlin.domain.bank.nh.utils.NhHeaderValueUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class GetFinAccountRequestDto(

    @field:JsonProperty("Header")
    val commonHeader: NhCommonRequestHeader,

    @field:JsonProperty("Rgno")
    val registerNumber: String,

    @field:JsonProperty("BrdtBrno")
    val birth: String
) {

    companion object {
        fun of(registerNumber: String): GetFinAccountRequestDto {

            val nowDateTimeParse = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd hhmmss"))
                .split(" ")

            return GetFinAccountRequestDto(
                NhCommonRequestHeader(
                    NhHeaderValueUtils.GET_FIN_ACCOUNT_API_NAME_VALUE,
                    nowDateTimeParse[0],
                    nowDateTimeParse[1],
                    NhHeaderValueUtils.AGENCY_CODE_VALUE,
                    NhHeaderValueUtils.FINTECH_NUMBER_VALUE,
                    NhHeaderValueUtils.FIN_ACCOUNT_SERVICE_CODE_VALUE,
                    NhHeaderValueUtils.createAgencyDealCode().toString(),
                    NhHeaderValueUtils.ACCESS_TOKEN_VALUE
                ),
                registerNumber,
                "19961025"
            )
        }
    }
}
