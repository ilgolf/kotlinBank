package me.golf.kotlin.domain.member.sms.application

import me.golf.kotlin.global.common.SingleCustomMessageSentResponse
import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.service.DefaultMessageService
import org.springframework.stereotype.Service

interface MessageService {

    fun init(): DefaultMessageService

    fun sendOne(message: Message): SingleCustomMessageSentResponse
}