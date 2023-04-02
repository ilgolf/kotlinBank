package me.golf.kotlin.domain.bank.payment.dto

import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.bank.nh.dto.NhCommonResponseHeader
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class SimplePaymentResponseDto(

    @field:JsonProperty("FinAcno")
    val finAccount: String,

    @field:JsonProperty("Header")
    val header: NhCommonResponseHeader,

    @field:JsonProperty("RgsnYmd")
    val registerDate: String
) {

    companion object {
        fun createDefault(finAccount: String): SimplePaymentResponseDto {
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

            return SimplePaymentResponseDto(
                finAccount,
                NhCommonResponseHeader(),
                formatter.format(LocalDate.now())
            )
        }
    }
}