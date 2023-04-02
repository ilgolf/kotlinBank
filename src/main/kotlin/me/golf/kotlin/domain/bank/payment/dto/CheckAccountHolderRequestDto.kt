package me.golf.kotlin.domain.bank.payment.dto

import me.golf.kotlin.domain.bank.model.BankName

data class CheckAccountHolderRequestDto(

    val bankName: BankName,
    val number: String
)
