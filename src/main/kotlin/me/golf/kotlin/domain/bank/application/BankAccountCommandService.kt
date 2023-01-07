package me.golf.kotlin.domain.bank.application

import me.golf.kotlin.domain.bank.dto.BankAccountSaveRequestDto
import me.golf.kotlin.domain.bank.dto.SimpleBankAccountIdResponseDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BankAccountCommandService {


    fun save(requestDto: BankAccountSaveRequestDto): SimpleBankAccountIdResponseDto {
        TODO("Not yet implemented")
    }

    fun update(nickname: String, bankAccountId: Long) {
        TODO("Not yet implemented")
    }

    fun delete(bankAccountId: Long, memberId: Long) {
        TODO("Not yet implemented")
    }
}
