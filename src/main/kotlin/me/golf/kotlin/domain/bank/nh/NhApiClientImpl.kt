package me.golf.kotlin.domain.bank.nh

import me.golf.kotlin.domain.bank.client.BankAccountApiClient
import me.golf.kotlin.domain.bank.dto.*
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

    override fun publishRegisterNumberConnection(publishRegisterNumberRequestDto: PublishRegisterNumberRequestDto): String =
        publishFinAccountHeadersSpec(NhUrlUtils.FIN_ACCOUNT_URL, publishRegisterNumberRequestDto)
            .retrieve()
            .bodyToMono(PublishFinAccountResponseDto::class.java)
            .flux()
            .toStream()
            .findFirst()
            .orElse(PublishFinAccountResponseDto("-1"))
            .registerNumber

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
            .orElse(BalanceNhResponseDto("????"))
            .balance

        return BalanceResponseDto(balance)
    }

    override fun getFinAccount(registerNumber: String): String {
        return getFinAccountRequestSpec(registerNumber)
            .flux()
            .toStream()
            .findFirst()
            .orElse(GetFinAccountResponseDto("-1")) // 미 발급된 경우 재발급을 추후에 할 수 있으므로 -1로 미발급 회원 구분
            .finAccount
    }

    private fun getFinAccountRequestSpec(registerNumber: String) = webClient
        .post()
        .uri(URI.create(NhUrlUtils.GET_FIN_ACCOUNT_URL))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(GetFinAccountRequestDto.of(registerNumber))
        .retrieve()
        .bodyToMono(GetFinAccountResponseDto::class.java)

    private fun getBalanceWithFinAccount(finAccount: String) =
        postRequestHeadersAndBodySpec(NhUrlUtils.FIND_BALANCE_URL, BalanceRequestDto.of(finAccount))
            .retrieve()
            .bodyToMono(BalanceNhResponseDto::class.java)

    private fun publishFinAccountHeadersSpec(
        url: String,
        finAccountRequestDto: PublishRegisterNumberRequestDto
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
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestDto)
}