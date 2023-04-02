package me.golf.kotlin.domain.bank.nh

import me.golf.kotlin.domain.bank.client.BankAccountApiClient
import me.golf.kotlin.domain.bank.dto.*
import me.golf.kotlin.domain.bank.nh.dto.*
import me.golf.kotlin.domain.bank.nh.utils.NhUrlUtils
import me.golf.kotlin.domain.bank.policy.DefaultValuePolicy.DEFAULT_BALANCE
import me.golf.kotlin.domain.bank.policy.DefaultValuePolicy.DEFAULT_NH_VALUE
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.net.URI
import java.util.stream.Collectors

@Component
@Profile("dev", "prd")
class NhApiClientImpl(
    private val webClient: WebClient
) : BankAccountApiClient {

    private val log = LoggerFactory.getLogger(NhApiClientImpl::class.java)

    override fun publishRegisterNumberConnection(publishRegisterNumberRequestDto: PublishRegisterNumberRequestDto): String =
        publishFinAccountHeadersSpec(publishRegisterNumberRequestDto)
            .flux()
            .toStream()
            .findFirst()
            .orElse(PublishRegisterNumberResponseDto.createDefault(DEFAULT_NH_VALUE))
            .registerNumber

    override fun getBalances(finAccounts: List<String>): MutableList<String> =
        finAccounts.stream()
            .map(this::getBalance)
            .map(BalanceResponseDto::balance)
            .collect(Collectors.toList())

    override fun getBalance(finAccount: String): BalanceResponseDto {
        val balance = postRequestHeadersAndBodySpec(BalanceRequestDto.of(finAccount))
            .flux()
            .toStream()
            .findFirst()
            .orElse(BalanceNhResponseDto.createDefault(DEFAULT_BALANCE))
            .balance

        return BalanceResponseDto(balance)
    }

    override fun getFinAccount(registerNumber: String): String {
        return getFinAccountRequestSpec(registerNumber)
            .flux()
            .toStream()
            .findFirst()
            .orElse(GetFinAccountResponseDto.createDefault(DEFAULT_NH_VALUE))
            .finAccount
    }

    private fun getFinAccountRequestSpec(registerNumber: String) = webClient
        .post()
        .uri(URI.create(NhUrlUtils.GET_FIN_ACCOUNT_URL))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(GetFinAccountRequestDto.of(registerNumber))
        .retrieve()
        .bodyToMono(GetFinAccountResponseDto::class.java)
        .onErrorResume {
            log.error("NH connection fail cause: {}", it.message)
            Mono.empty()
        }


    private fun publishFinAccountHeadersSpec(
        finAccountRequestDto: PublishRegisterNumberRequestDto
    ) = webClient
        .post()
        .uri(URI.create(NhUrlUtils.FIN_ACCOUNT_URL))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(finAccountRequestDto)
        .retrieve()
        .bodyToMono(PublishRegisterNumberResponseDto::class.java)
        .onErrorResume {
            log.error("NH connection fail cause: {}", it.message)
            Mono.empty()
        }

    private fun postRequestHeadersAndBodySpec(
        requestDto: BalanceRequestDto
    ) = webClient
        .post()
        .uri(URI(NhUrlUtils.FIND_BALANCE_URL))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestDto)
        .retrieve()
        .bodyToMono(BalanceNhResponseDto::class.java)
        .onErrorResume {
            log.error("NH connection fail cause: {}", it.message)
            Mono.empty()
        }
}