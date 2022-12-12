package me.golf.kotlin.domain.member.sms.repository

import me.golf.kotlin.global.common.RedisPolicy
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Repository
class AuthNumberRedisRepository(
    private val redisTemplate: RedisTemplate<String, Int>
): AuthNumberRepository {

    private val log = LoggerFactory.getLogger(AuthNumberRedisRepository::class.java)

    @Transactional
    override fun saveAuthNumber(key: String, secureNumber: Int) {
        val opsForHash = redisTemplate.opsForHash<String, Int>()

        log.info("[POST] key : {}", key)

        redisTemplate.expire(RedisPolicy.PHONE_AUTH_KEY, 5, TimeUnit.MINUTES)
        opsForHash.put(RedisPolicy.PHONE_AUTH_KEY, key, secureNumber)
    }

    @Transactional(readOnly = true)
    override fun getAuthNumber(key: String) =
        redisTemplate.opsForHash<String, Int>().get(RedisPolicy.PHONE_AUTH_KEY, key)

}