package me.golf.kotlin.domain.bank.model

import me.golf.kotlin.domain.bank.error.BankAccountException

enum class BankName(
    val value: String,
    val code: String
) {

    KB("국민은행", "004"),
    NH("농협은행", "011"),
    SIN_HAN("신한은행", "088"),
    HANA("하나은행", "081"),
    IBK("기업은행", "003"),
    WOORI("우리은행", "");

    companion object {
        fun of(bankName: String): BankName {
            return when (bankName) {
                KB.value -> KB
                NH.value -> NH
                SIN_HAN.value -> SIN_HAN
                HANA.value -> HANA
                IBK.value -> IBK
                WOORI.value -> WOORI
                else -> throw BankAccountException.ConvertBankNameDeniedException()
            }
        }
    }
}
