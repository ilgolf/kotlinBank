package me.golf.kotlin.domain.bank.payment.client

import me.golf.kotlin.domain.bank.payment.dto.RefundMessageRequestDto
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
@Profile("local", "test")
class DefaultPaymentApiClient: PaymentApiClient {

    override fun pay(finAccount: String, transferMoney: BigDecimal): String {
        return ""
    }

    override fun refund(requestDto: RefundMessageRequestDto): String {
        return ""
    }
}