package me.golf.kotlin.domain.bank.dto

import me.golf.kotlin.domain.bank.model.BankName
import javax.validation.constraints.NotBlank

data class BankAccountSaveApiRequestDto(

    @field:NotBlank(message = "필수 값입니다.")
    val name: String,

    @field:NotBlank(message = "필수 값입니다.")
    val bankName: String,

    @field:NotBlank(message = "필수 값입니다.")
    val password: String,

    @field:NotBlank(message = "필수 값입니다.")
    val number: String
) {

    fun toServiceDto(memberId: Long): BankAccountSaveRequestDto {
        return BankAccountSaveRequestDto(
            name = this.name,
            bankName = BankName.of(this.bankName),
            password = this.password,
            number = this.number,
            memberId = memberId
        )
    }
}
