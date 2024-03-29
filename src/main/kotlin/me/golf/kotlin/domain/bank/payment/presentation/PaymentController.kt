package me.golf.kotlin.domain.bank.payment.presentation

import me.golf.kotlin.domain.bank.LookupType
import me.golf.kotlin.domain.bank.RequireFinAccount
import me.golf.kotlin.domain.bank.payment.application.PaymentService
import me.golf.kotlin.domain.bank.payment.dto.PaymentApiRequestDto
import me.golf.kotlin.domain.bank.payment.dto.RefundApiRequestDto
import me.golf.kotlin.domain.bank.payment.dto.SimpleTransferResponseDto
import me.golf.kotlin.global.security.CustomUserDetails
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v2/bank-accounts")
class PaymentController(
    private val paymentService: PaymentService
) {

    private val log = LoggerFactory.getLogger(PaymentController::class.java)

    companion object {
        const val SUCCESS_MESSAGE = "결제 요청에 성공했습니다."
    }

    @PostMapping("/payment")
    fun pay(
        @Valid @RequestBody requestDto: PaymentApiRequestDto,
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ResponseEntity<SimpleTransferResponseDto> {

        paymentService.pay(requestDto.toServiceDto(customUserDetails.memberId))
        return ResponseEntity.ok(SimpleTransferResponseDto(SUCCESS_MESSAGE))
    }

    @PostMapping("/refunds")
    fun refund(
        @Valid @RequestBody requestDto: RefundApiRequestDto,
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ResponseEntity<SimpleTransferResponseDto> {

        paymentService.refund(requestDto.toServiceDto(customUserDetails.memberId))
        return ResponseEntity.ok(SimpleTransferResponseDto(SUCCESS_MESSAGE))
    }
}
