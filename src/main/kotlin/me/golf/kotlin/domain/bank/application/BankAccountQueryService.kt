package me.golf.kotlin.domain.bank.application

import me.golf.kotlin.domain.bank.client.BankAccountApiClient
import me.golf.kotlin.domain.bank.dto.BalanceResponseDto
import me.golf.kotlin.domain.bank.dto.BankAccountDetailResponseDto
import me.golf.kotlin.domain.bank.dto.BankAccountSummaryResponseDto
import me.golf.kotlin.domain.bank.dto.BankAccountSummaryWithFinAccount
import me.golf.kotlin.domain.bank.error.BankAccountException
import me.golf.kotlin.domain.bank.history.application.TransferHistoryService
import me.golf.kotlin.domain.bank.model.BankAccountRedisRepository
import me.golf.kotlin.domain.bank.model.BankAccountRepository
import me.golf.kotlin.domain.bank.nh.utils.NhHeaderValueUtils
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
@Transactional(readOnly = true)
class BankAccountQueryService(
    private val bankAccountRepository: BankAccountRepository,
    private val bankAccountApiClient: BankAccountApiClient,
    private val bankAccountRedisRepository: BankAccountRedisRepository,
) {

    fun getBankAccountSummary(bankAccountId: Long, memberId: Long): BankAccountDetailResponseDto {
        val bankAccount = getBankAccount(bankAccountId, memberId)
        val balance = bankAccountRedisRepository.findBalanceByFinAccount(bankAccount.finAccount) ?: "????"

        return BankAccountDetailResponseDto(bankAccount, balance)
    }

    fun getBalance(bankAccountId: Long, memberId: Long): BalanceResponseDto {
        val bankAccount = getBankAccount(bankAccountId, memberId)

        return bankAccountApiClient.getBalance(bankAccount.finAccount)
    }

    fun getBankAccountsByMemberId(memberId: Long): List<BankAccountSummaryResponseDto> {
        val summaryWithFinAccounts = getBankAccountSummaryWithFinAccounts(memberId)
        val finAccounts = summaryWithFinAccounts.stream().map { dto -> dto.finAccount }.collect(Collectors.toList())

        val balancesByFinAccount = bankAccountRedisRepository.findBalancesByFinAccount(finAccounts)

        return BankAccountSummaryResponseDto.of(summaryWithFinAccounts, balancesByFinAccount)
    }

    fun updateBalanceByMemberId(memberId: Long): List<String> {
        val finAccounts = getBankAccountSummaryWithFinAccounts(memberId).stream()
            .map(BankAccountSummaryWithFinAccount::finAccount)
            .collect(Collectors.toList())

        return bankAccountApiClient.getBalances(finAccounts)
    }

    private fun getBankAccountSummaryWithFinAccounts(memberId: Long) = bankAccountRepository.findAllByMemberId(memberId)

    private fun getBankAccount(
        bankAccountId: Long,
        memberId: Long
    ) =
        bankAccountRepository.findByIdAndMemberId(bankAccountId, memberId)
            ?: throw BankAccountException.NotFoundException()
}
