package me.golf.kotlin.domain.member.sms.application

import me.golf.kotlin.domain.member.sms.error.SecureNumberNotFoundException
import me.golf.kotlin.domain.member.sms.repository.AuthNumberRepository
import me.golf.kotlin.global.common.RedisPolicy
import me.golf.kotlin.global.common.SingleCustomMessageSentResponse
import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.model.MessageType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom
import java.time.Duration

@Service
class SmsService(
    @param:Value("\${cool-sms.phone-number}") private val PHONE_NUMBER: String,
    private val messageService: MessageService,
    private val authNumberRepository: AuthNumberRepository
) {

    private val log = LoggerFactory.getLogger(SmsService::class.java)

    @Transactional
    fun sendAuthNumber(phoneNumber: String): SingleCustomMessageSentResponse {
        val randomNumber = StringBuilder()
        val secureRandom = SecureRandom()

        for (i in 1..5) {
            val number = secureRandom.nextInt(10)
            randomNumber.append(number)
        }

        log.info("random number : {}", randomNumber)

        val message = Message(
            from = PHONE_NUMBER, to = phoneNumber, type = MessageType.SMS, text = "인증 번호는 [$randomNumber] 입니다.")

        authNumberRepository.saveAuthNumber(phoneNumber.substring(4), randomNumber.toString().toInt())

        return messageService.sendOne(message)
    }

    fun authorizePhoneNumber(phoneNumber: String, authNumber: Int): Boolean {

        val secureNumber = authNumberRepository.getAuthNumber(phoneNumber.substring(4))
            ?: throw SecureNumberNotFoundException(phoneNumber)

        return secureNumber == authNumber
    }
}