package me.golf.kotlin.domain.bank.model

import org.springframework.data.jpa.repository.JpaRepository

interface BankAccountRepository: JpaRepository<BankAccount, Long>, BankAccountCustomRepository {

    fun findByIdAndMemberId(id: Long, memberId: Long): BankAccount?

    fun existsByName(name: String): Boolean

    fun existsByNumber(number: String): Boolean

    fun countByNumberAndName(number: String, name: String): Int
}