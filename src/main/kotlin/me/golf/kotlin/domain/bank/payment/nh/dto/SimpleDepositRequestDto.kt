package me.golf.kotlin.domain.bank.payment.nh.dto

import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.bank.nh.dto.NhCommonRequestHeader
import me.golf.kotlin.domain.bank.nh.utils.NhHeaderValueUtils
import me.golf.kotlin.domain.bank.payment.dto.RefundMessageRequestDto
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class SimpleDepositRequestDto (


    @field:JsonProperty("Header")
    val commonRequestHeader: NhCommonRequestHeader,

    @field:JsonProperty("Bncd")
    val bankCode: String,

    @field:JsonProperty("Acno")
    val accountNumber: String,

    @field:JsonProperty("Tram")
    val transferMoney: BigDecimal,

    @field:JsonProperty("MracOtlt")
    val refundCause: String
) {

    companion object {
        fun of(requestDto: RefundMessageRequestDto): SimpleDepositRequestDto {
            val nowDateTimeParse = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd hhmmss"))
                .split(" ")

            return SimpleDepositRequestDto(
                NhCommonRequestHeader(
                    NhHeaderValueUtils.REFUND_API_NAME_VALUE,
                    nowDateTimeParse[0],
                    nowDateTimeParse[1],
                    NhHeaderValueUtils.AGENCY_CODE_VALUE,
                    NhHeaderValueUtils.FINTECH_NUMBER_VALUE,
                    NhHeaderValueUtils.BALANCE_SERVICE_CODE_VALUE,
                    NhHeaderValueUtils.createAgencyDealCode().toString(),
                    NhHeaderValueUtils.ACCESS_TOKEN_VALUE
                ),
                requestDto.bankName.code,
                requestDto.accountNumber,
                requestDto.transferMoney,
                requestDto.refundCause
            )
        }
    }
}
