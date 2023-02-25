package me.golf.kotlin.domain.bank.nh.utils

import java.security.SecureRandom

object NhHeaderValueUtils {

    const val GET_API_NAME_VALUE = "OpenFinAccountDirect"
    const val AGENCY_CODE_VALUE = "001697"
    const val FINTECH_NUMBER_VALUE = "001"
    const val SERVICE_CODE_VALUE = "DrawingTransferA"
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