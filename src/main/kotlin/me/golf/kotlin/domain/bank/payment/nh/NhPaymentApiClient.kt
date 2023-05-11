package me.golf.kotlin.domain.bank.payment.nh

import me.golf.kotlin.domain.bank.nh.utils.NhUrlUtils
import me.golf.kotlin.domain.bank.payment.client.PaymentApiClient
import me.golf.kotlin.domain.bank.payment.dto.RefundMessageRequestDto
import me.golf.kotlin.domain.bank.payment.dto.RefundRequestDto
import me.golf.kotlin.domain.bank.payment.dto.SimplePaymentResponseDto
import me.golf.kotlin.domain.bank.payment.dto.SimpleRefundResponseDto
import me.golf.kotlin.domain.bank.payment.exception.SimplePaymentFailException
import me.golf.kotlin.domain.bank.payment.nh.dto.SimpleDepositRequestDto
import me.golf.kotlin.domain.bank.payment.nh.dto.SimplePaymentRequestDto
import me.golf.kotlin.domain.bank.policy.DefaultValuePolicy.DEFAULT_NH_VALUE
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.net.URI

@Component
@Profile("dev", "prd")
class NhPaymentApiClient(
    private val webClient: WebClient
): PaymentApiClient {

    companion object {
        private val log = LoggerFactory.getLogger(NhPaymentApiClient::class.java)
    }

    override fun pay(finAccount: String, transferMoney: BigDecimal): String {
        return webClient.post()
            .uri(URI.create(NhUrlUtils.PAYMENT_URL))
            .bodyValue(SimplePaymentRequestDto.of(finAccount, transferMoney))
            .retrieve()
            .onStatus(
                { status -> status.is4xxClientError || status.is5xxServerError },
                { response ->
                    Mono.error(
                        SimplePaymentFailException(response.statusCode().reasonPhrase)
                    )
                }
            )
            .bodyToMono<SimplePaymentResponseDto>()
            .onErrorResume { error ->
                log.error("Payment failed: {}", error.message)
                Mono.just(SimplePaymentResponseDto.createDefault(finAccount))
            }
            .flux()
            .toStream()
            .findFirst()
            .orElseGet { SimplePaymentResponseDto.createDefault(finAccount) }
            .header
            .responseMessage
    }

    override fun refund(requestDto: RefundMessageRequestDto): String {
        return webClient.post()
            .uri(URI.create(NhUrlUtils.DEPOSIT_URL))
            .bodyValue(SimpleDepositRequestDto.of(requestDto))
            .retrieve()
            .onStatus(
                { status -> status.is4xxClientError || status.is5xxServerError },
                { response ->
                    Mono.error(
                        SimplePaymentFailException(response.statusCode().reasonPhrase)
                    )
                }
            )
            .bodyToMono<SimpleRefundResponseDto>()
            .onErrorResume { error ->
                log.error("Refund failed: {}", error.message)
                Mono.just(SimpleRefundResponseDto.createDefault())
            }
            .flux()
            .toStream()
            .findFirst()
            .orElseGet { SimpleRefundResponseDto.createDefault() }
            .header
            .responseMessage
    }
}