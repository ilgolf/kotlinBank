package me.golf.kotlin.domain.bank.payment.exception

import me.golf.kotlin.global.exception.error.BusinessException
import me.golf.kotlin.global.exception.error.ErrorCode

class SimplePaymentFailException(reasonPhrase: String): BusinessException(ErrorCode.PAYMENT_FAIL, reasonPhrase)