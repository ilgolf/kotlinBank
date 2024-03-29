package me.golf.kotlin.domain.bank.client

import me.golf.kotlin.domain.bank.dto.BalanceResponseDto
import me.golf.kotlin.domain.bank.nh.dto.PublishRegisterNumberRequestDto

interface BankAccountApiClient {

    fun publishRegisterNumberConnection(publishRegisterNumberRequestDto: PublishRegisterNumberRequestDto): String

    fun getBalances(finAccounts: List<String>): List<String>

    fun getBalance(finAccount: String): BalanceResponseDto

    fun getFinAccount(registerNumber: String): String
}
