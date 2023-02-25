package me.golf.kotlin.domain.bank.nh

import me.golf.kotlin.domain.bank.client.BankAccountApiClient
import me.golf.kotlin.domain.bank.dto.BalanceRequestDto
import me.golf.kotlin.domain.bank.dto.BalanceResponseDto
import me.golf.kotlin.domain.bank.dto.PublishFinAccountRequestDto
import me.golf.kotlin.domain.bank.error.BankAccountException
import me.golf.kotlin.domain.bank.nh.utils.NhUrlUtils
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.net.URI
import java.util.stream.Collectors

@Component
@Profile("dev", "prd")
class NhApiClientImpl(
    private val webClient: WebClient
) : BankAccountApiClient {

    override fun getFinAccountConnection(finAccountRequestDto: PublishFinAccountRequestDto): String =
        publishFinAccountHeadersSpec(NhUrlUtils.FIN_ACCOUNT_URL, finAccountRequestDto)
            .retrieve()
            .bodyToMono(String::class.java)
            .flux()
            .toStream()
            .findFirst()
            .orElseThrow { throw BankAccountException.FinAccountNotFoundException() }

    override fun getBalances(finAccounts: List<String>): MutableList<String> =
        finAccounts.stream()
            .map(this::getBalance)
            .map(BalanceResponseDto::balance)
            .collect(Collectors.toList())

    override fun getBalance(finAccount: String): BalanceResponseDto {
        val balance = getBalanceWithFinAccount(finAccount)
            .flux()
            .toStream()
            .findFirst()
            .orElse("????")

        return BalanceResponseDto(balance)
    }

    private fun getBalanceWithFinAccount(finAccount: String) =
        postRequestHeadersAndBodySpec(NhUrlUtils.FIND_BALANCE_URL, BalanceRequestDto.of(finAccount))
            .retrieve()
            .bodyToMono(String::class.java)

    private fun publishFinAccountHeadersSpec(
        url: String,
        finAccountRequestDto: PublishFinAccountRequestDto
    ) = webClient
        .post()
        .uri(URI.create(url))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(finAccountRequestDto)

    private fun postRequestHeadersAndBodySpec(
        url: String,
        requestDto: BalanceRequestDto
    ) = webClient
        .post()
        .uri(URI(url))
        .bodyValue(requestDto)
}