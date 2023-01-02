package me.golf.kotlin.domain.member.sms.model

import lombok.Getter
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@Getter
@RedisHash("PHONE_NUMBER")
class AuthNumber(

    @Id
    var id: String, // phone-number에서 010을 제외
    var authNumber: Int
)