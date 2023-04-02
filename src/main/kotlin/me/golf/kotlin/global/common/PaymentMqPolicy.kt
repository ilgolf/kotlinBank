package me.golf.kotlin.global.common

object PaymentMqPolicy {

    const val PAYMENT_QUEUE = "paymentQueue"
    const val REFUND_QUEUE = "refundQueue"
    const val PAYMENT_DEAD_LETTER_QUEUE = "paymentDeadLetterQueue"
    const val REFUND_DEAD_LETTER_QUEUE = "refundDeadLetterQueue"
}