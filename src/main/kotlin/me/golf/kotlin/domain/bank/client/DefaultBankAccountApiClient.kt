package me.golf.kotlin.domain.bank.client

import me.golf.kotlin.domain.bank.dto.BalanceResponseDto
import me.golf.kotlin.domain.bank.dto.PublishFinAccountRequestDto
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("local", "test")
class DefaultBankAccountApiClient: BankAccountApiClient {

    override fun getFinAccountConnection(finAccountRequestDto: PublishFinAccountRequestDto): String {
        return "123456789"
    }

    override fun getBalances(finAccounts: List<String>): List<String> {
        return arrayListOf()
    }

    override fun getBalance(finAccount: String): BalanceResponseDto {
        return BalanceResponseDto("balance")
    }
}