package me.golf.kotlin.domain.bank.payment.dto

import me.golf.kotlin.domain.bank.model.BankName
import javax.validation.constraints.NotBlank

data class CheckAccountHolderApiRequestDto(

    @NotBlank(message = "필수 값입니다.")
    val bankName: String,

    @NotBlank(message = "필수 값입니다.")
    val number: String
) {
    fun toServiceDto() = CheckAccountHolderRequestDto(
        bankName = BankName.of(bankName),
        number = this.number
    )
}