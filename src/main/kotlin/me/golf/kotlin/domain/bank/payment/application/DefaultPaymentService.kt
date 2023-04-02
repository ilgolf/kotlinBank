package me.golf.kotlin.domain.bank.payment.application

import me.golf.kotlin.domain.bank.payment.dto.RefundRequestDto
import me.golf.kotlin.domain.bank.payment.dto.TransferRequestDto
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("local", "test")
class DefaultPaymentService: PaymentService {

    override fun pay(requestDto: TransferRequestDto) {
    }

    override fun refund(requestDto: RefundRequestDto) {
    }
}