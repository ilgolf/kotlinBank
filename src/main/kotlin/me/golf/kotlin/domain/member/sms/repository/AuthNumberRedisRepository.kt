package me.golf.kotlin.domain.member.sms.repository

import me.golf.kotlin.global.common.RedisPolicy
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository

@Repository
class AuthNumberRedisRepository(
    private val redisTemplate: StringRedisTemplate
): AuthNumberRepository {

    override fun save(key: String, authNumber: String) {
        redisTemplate.opsForHash<String, String>().put(RedisPolicy.PHONE_AUTH_KEY, key, authNumber)
    }

    override fun findByIdOrNull(key: String): String? {
        return redisTemplate.opsForHash<String, String>().get(RedisPolicy.PHONE_AUTH_KEY, key)
    }
}