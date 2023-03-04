package me.golf.kotlin.domain.bank.client

import me.golf.kotlin.domain.bank.dto.BalanceResponseDto
import me.golf.kotlin.domain.bank.dto.GetFinAccountResponseDto
import me.golf.kotlin.domain.bank.dto.PublishRegisterNumberRequestDto
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("local", "test")
class DefaultBankAccountApiClient: BankAccountApiClient {

    override fun publishRegisterNumberConnection(publishRegisterNumberRequestDto: PublishRegisterNumberRequestDto): String {
        return "123456789"
    }

    override fun getBalances(finAccounts: List<String>): List<String> {
        return arrayListOf()
    }

    override fun getBalance(finAccount: String): BalanceResponseDto {
        return BalanceResponseDto("finAccount")
    }

    override fun getFinAccount(registerNumber: String): String {
        return GetFinAccountResponseDto.createDefault("finAccount").finAccount
    }
}