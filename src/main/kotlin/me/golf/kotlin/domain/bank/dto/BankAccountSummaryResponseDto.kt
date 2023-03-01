package me.golf.kotlin.domain.bank.dto

import java.util.stream.Collectors

data class BankAccountSummaryResponseDto(
    val bankAccountId: Long,
    val balance: String,
    val bankName: String
) {
    companion object {

        fun of(summary: List<BankAccountSummaryWithFinAccount>, balances: Map<String, String>): List<BankAccountSummaryResponseDto> {
            val summaryMap: Map<String, BankAccountSummaryWithFinAccount> = summary.stream()
                .collect(Collectors.toMap(BankAccountSummaryWithFinAccount::finAccount, BankAccountSummaryWithFinAccount::getMe))

            val resultDtos = arrayListOf<BankAccountSummaryResponseDto>()

            for (key in summaryMap.keys) {
                val dto = summaryMap[key]

                if (dto != null && balances.containsKey(key)) {
                    val value = balances[key]

                    if (value != null) {
                        resultDtos.add(dto.toApiResponseDto(value))
                    }
                }
            }

            return resultDtos
        }
    }
}