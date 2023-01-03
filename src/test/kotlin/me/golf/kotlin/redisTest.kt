package me.golf.kotlin

import me.golf.kotlin.commonutil.IntegrationTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate

class RedisTemplateTest

@Autowired
constructor(
    private val redisTemplate: StringRedisTemplate
) : IntegrationTest() {

    @BeforeEach
    fun init() {
        redisTemplate.opsForHash<String, String>().put("KEY", "1234", "1234")
    }

    @Test
    fun get() {
        val get = redisTemplate.opsForHash<String, String>().get("KEY", "1234")?: throw IllegalArgumentException("exception")

        print(get)
    }
}