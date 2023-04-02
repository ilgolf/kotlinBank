package me.golf.kotlin.domain.bank.payment.client

import me.golf.kotlin.domain.bank.payment.dto.RefundMessageRequestDto
import java.math.BigDecimal

interface PaymentApiClient {

    fun pay(finAccount: String, transferMoney: BigDecimal): String
    fun refund(requestDto: RefundMessageRequestDto): String
}
