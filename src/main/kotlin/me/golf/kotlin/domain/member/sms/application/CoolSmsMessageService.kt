package me.golf.kotlin.domain.member.sms.application

import me.golf.kotlin.domain.member.sms.error.SendFailException
import me.golf.kotlin.global.common.SingleCustomMessageSentResponse
import net.nurigo.sdk.NurigoApp.initialize
import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.request.SingleMessageSendingRequest
import net.nurigo.sdk.message.service.DefaultMessageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class CoolSmsMessageService(
    @param:Value("\${cool-sms.api-key}") private val API_KEY: String,
    @param:Value("\${cool-sms.api-secret}") private val API_SECRET: String,
    @param:Value("\${cool-sms.domain}") private val DOMAIN: String
) : MessageService {

    override fun init(): DefaultMessageService {
        return initialize(API_KEY, API_SECRET, DOMAIN)
    }

    override fun sendOne(message: Message): SingleCustomMessageSentResponse {
        val response = this.init().sendOne(SingleMessageSendingRequest(message))
            ?: throw SendFailException(message.to?: "")

        return SingleCustomMessageSentResponse.of(response)
    }
}