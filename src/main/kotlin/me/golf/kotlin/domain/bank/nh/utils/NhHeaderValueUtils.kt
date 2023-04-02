package me.golf.kotlin.domain.bank.nh.utils

import java.security.SecureRandom

object NhHeaderValueUtils {

    const val FIN_ACCOUNT_API_NAME_VALUE = "OpenFinAccountDirect"
    const val BALANCE_API_NAME_VALUE = "InquireBalance"
    const val PAYMENT_API_NAME_VALUE = "DrawingTransfer"
    const val REFUND_API_NAME_VALUE = "ReceivedTransferAccountNumber"
    const val GET_FIN_ACCOUNT_API_NAME_VALUE = "CheckOpenFinAccountDirect"
    const val AGENCY_CODE_VALUE = "001697"
    const val FINTECH_NUMBER_VALUE = "001"
    const val FIN_ACCOUNT_SERVICE_CODE_VALUE = "DrawingTransferA"
    const val BALANCE_SERVICE_CODE_VALUE = "ReceivedTransferA"
    const val ACCESS_TOKEN_VALUE = "c6d6c59b1a34c1372d12c206e84498701672124b346ae57482321abe39c40ded"

    fun createAgencyDealCode(): Int {
        val randomNumber = StringBuilder()
        val secureRandom = SecureRandom()

        for (i in 1..9) {
            val number = secureRandom.nextInt(10)
            randomNumber.append(number)
        }

        return randomNumber.toString().toInt()
    }
}