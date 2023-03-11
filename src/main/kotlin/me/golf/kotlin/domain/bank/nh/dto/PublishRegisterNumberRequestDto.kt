package me.golf.kotlin.domain.bank.nh.dto

import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.bank.nh.utils.NhHeaderValueUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class PublishRegisterNumberRequestDto(

    @field:JsonProperty("Header")
    val commonHeader: NhCommonRequestHeader,

    @field:JsonProperty("DrtrRgyn")
    val withdrawalTransferYn: String,

    @field:JsonProperty("BrdtBrno")
    val birth: String,

    @field:JsonProperty("Bncd")
    val bankCode: String,

    @field:JsonProperty("Acno")
    val accountNumber: String
) {
    companion object {
        fun of(drtrRgyn: Boolean, bnCd: String, acno: String): PublishRegisterNumberRequestDto {

            val nowDateTimeParse = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd hhmmss"))
                .split(" ")

            return PublishRegisterNumberRequestDto(
                NhCommonRequestHeader(
                    NhHeaderValueUtils.FIN_ACCOUNT_API_NAME_VALUE,
                    nowDateTimeParse[0],
                    nowDateTimeParse[1],
                    NhHeaderValueUtils.AGENCY_CODE_VALUE,
                    NhHeaderValueUtils.FINTECH_NUMBER_VALUE,
                    NhHeaderValueUtils.FIN_ACCOUNT_SERVICE_CODE_VALUE,
                    NhHeaderValueUtils.createAgencyDealCode().toString(),
                    NhHeaderValueUtils.ACCESS_TOKEN_VALUE
                ),
                if (drtrRgyn) "Y" else "N",
                "19961025", // 농협에서 내 생일에 대해서만 API를 제공해주므로 Default 처럼 가져가는걸로
                bnCd,
                acno
            )
        }
    }
}