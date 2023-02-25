package me.golf.kotlin.domain.bank.dto

import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.bank.nh.utils.NhHeaderValueUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class PublishFinAccountRequestDto(

    @field:JsonProperty("Header")
    val commonHeader: NhCommonHeader,

    @field:JsonProperty("DrtrRgyn")
    val withdrawalTransferYn: String,

    @field:JsonProperty("BrdtBrno")
    val birth: String,

    @field:JsonProperty("BnCd")
    val bankCode: String,

    @field:JsonProperty("Acno")
    val accountNumber: String
) {
    companion object {
        fun of(drtrRgyn: Boolean, brdtBrno: String, bnCd: String, acno: String): PublishFinAccountRequestDto {

            val nowDateTimeParse = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd hhmmss"))
                .split(" ")

            return PublishFinAccountRequestDto(
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
                if (drtrRgyn) "Y" else "N",
                brdtBrno,
                bnCd,
                acno
            )
        }
    }
}