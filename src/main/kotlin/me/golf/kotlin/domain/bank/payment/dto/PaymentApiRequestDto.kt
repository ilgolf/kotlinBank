package me.golf.kotlin.domain.bank.payment.dto

import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class PaymentApiRequestDto(

    @NotNull(message = "필수 값입니다.")
    val bankId: Long,

    @NotBlank(message = "필수 값입니다.")
    val fromName: String,

    @NotBlank(message = "필수 값입니다.")
    val transferMoney: String
) {

    fun toServiceDto(memberId: Long): TransferRequestDto {
        return TransferRequestDto(
            bankId = this.bankId,
            fromName = this.fromName,
            transferMoney = BigDecimal(transferMoney),
            memberId = memberId
        )
    }
}
