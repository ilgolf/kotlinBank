package me.golf.kotlin.domain.bank.dto

data class BankAccountSaveApiRequestDto(
    val name: String,
    val bankName: String,
    val password: String,
    val number: String
) {

    fun toServiceDto(memberId: Long): BankAccountSaveRequestDto {
        return BankAccountSaveRequestDto(
            name = this.name,
            bankName = this.bankName,
            password = this.password,
            number = this.number,
            memberId = memberId
        )
    }
}
