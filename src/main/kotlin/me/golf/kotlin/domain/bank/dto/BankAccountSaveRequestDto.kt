package me.golf.kotlin.domain.bank.dto

data class BankAccountSaveRequestDto (
    val name: String,
    val bankName: String,
    val password: String,
    val number: String,
    val memberId: Long
)
