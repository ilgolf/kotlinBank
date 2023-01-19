package me.golf.kotlin.domain.bank.model

import org.springframework.data.jpa.repository.JpaRepository

interface BankAccountRepository: JpaRepository<BankAccount, Long> {

    fun findByIdAndMemberId(id: Long, memberId: Long): BankAccount?
}