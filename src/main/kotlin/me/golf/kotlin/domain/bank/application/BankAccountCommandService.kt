package me.golf.kotlin.domain.bank.application

import me.golf.kotlin.domain.bank.client.NHApiClient
import me.golf.kotlin.domain.bank.dto.BankAccountSaveRequestDto
import me.golf.kotlin.domain.bank.dto.SimpleBankAccountIdResponseDto
import me.golf.kotlin.domain.bank.error.FinAccountNotFoundException
import me.golf.kotlin.domain.bank.model.BankAccountRepository
import me.golf.kotlin.domain.bank.utils.NhHeaderValueUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.security.auth.login.AccountNotFoundException

@Service
@Transactional
class BankAccountCommandService(
    private val bankAccountRepository: BankAccountRepository,
    private val encoder: PasswordEncoder,
    private val nhApiClient: NHApiClient
) {

    fun save(requestDto: BankAccountSaveRequestDto): SimpleBankAccountIdResponseDto {

        val randomNumber = StringBuilder()
        val secureRandom = SecureRandom()

        for (i in 1..9) {
            val number = secureRandom.nextInt(10)
            randomNumber.append(number)
        }

        val finAccount = nhApiClient.getFinAccountConnection(randomNumber.toString())
            .flux()
            .toStream()
            .findFirst()
            .orElseThrow { throw FinAccountNotFoundException() }

        val bankAccount = requestDto.toEntity(finAccount).encodePassword(encoder)

        return SimpleBankAccountIdResponseDto(bankAccountRepository.save(bankAccount).id)
    }

    fun update(nickname: String, bankAccountId: Long, memberId: Long) {
        val bankAccount = bankAccountRepository.findByIdAndMemberId(bankAccountId, memberId)
            ?: throw AccountNotFoundException()

        bankAccount.updateAccountName(nickname)
    }

    fun delete(bankAccountId: Long, memberId: Long) {
        val bankAccount = bankAccountRepository.findByIdAndMemberId(bankAccountId, memberId)
            ?: throw AccountNotFoundException()

        bankAccount.delete()
    }
}
