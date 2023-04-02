package me.golf.kotlin.domain.bank.payment.application

import me.golf.kotlin.domain.bank.payment.dto.RefundRequestDto
import me.golf.kotlin.domain.bank.payment.dto.TransferRequestDto

interface PaymentService {

    fun pay(requestDto: TransferRequestDto)

    fun refund(requestDto: RefundRequestDto)
}