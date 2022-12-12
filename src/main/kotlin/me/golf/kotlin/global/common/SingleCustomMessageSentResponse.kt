package me.golf.kotlin.global.common

import net.nurigo.sdk.message.response.SingleMessageSentResponse

data class SingleCustomMessageSentResponse(
    val to: String,
    val from: String,
    val country: String,
) {

    companion object {
        fun of(response: SingleMessageSentResponse) =
            SingleCustomMessageSentResponse(response.to, response.from, response.country)
    }
}