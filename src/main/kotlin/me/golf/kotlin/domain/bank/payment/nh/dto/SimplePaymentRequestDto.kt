package me.golf.kotlin.domain.bank.payment.nh.dto

import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.bank.nh.dto.NhCommonRequestHeader
import me.golf.kotlin.domain.bank.nh.utils.NhHeaderValueUtils
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SimplePaymentRequestDto(

    @field:JsonProperty("Header")
    val nhHeader: NhCommonRequestHeader,

    @field:JsonProperty("FinAcno")
    val finAccount: String,

    @field:JsonProperty("Tram")
    val transferMoney: BigDecimal
) {

    companion object {
        fun of(finAccount: String, transferMoney: BigDecimal): SimplePaymentRequestDto {
            val nowDateTimeParse = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd hhmmss"))
                .split(" ")

            return SimplePaymentRequestDto(
                NhCommonRequestHeader(
                    NhHeaderValueUtils.PAYMENT_API_NAME_VALUE,
                    nowDateTimeParse[0],
                    nowDateTimeParse[1],
                    NhHeaderValueUtils.AGENCY_CODE_VALUE,
                    NhHeaderValueUtils.FINTECH_NUMBER_VALUE,
                    NhHeaderValueUtils.FIN_ACCOUNT_SERVICE_CODE_VALUE,
                    NhHeaderValueUtils.createAgencyDealCode().toString(),
                    NhHeaderValueUtils.ACCESS_TOKEN_VALUE
                ),
                finAccount,
                transferMoney
            )
        }
    }
}
