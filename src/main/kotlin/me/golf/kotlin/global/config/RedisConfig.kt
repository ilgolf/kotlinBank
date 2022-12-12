package me.golf.kotlin.global.config

import me.golf.kotlin.global.common.RedisPolicy
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.time.Duration


@Configuration
@EnableTransactionManagement
class RedisConfig(
    @Value("\${spring.redis.host}") private val host: String,
    @Value("\${spring.redis.port}") private val port: Int,
) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory = LettuceConnectionFactory(host, port)

    @Bean
    fun stringRedisTemplate(): StringRedisTemplate {
        val template = StringRedisTemplate(redisConnectionFactory())

        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = StringRedisSerializer()

        template.hashKeySerializer = StringRedisSerializer()
        template.hashValueSerializer = GenericToStringSerializer(Int.Companion::class.java)

        template.setEnableTransactionSupport(true)

        return template
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Int> {
        val redisTemplate = RedisTemplate<String, Int>()

        redisTemplate.setConnectionFactory(redisConnectionFactory())

        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.hashKeySerializer = StringRedisSerializer()

        redisTemplate.hashValueSerializer = JdkSerializationRedisSerializer()

        return redisTemplate
    }

    @Bean
    fun redisCacheManager(): RedisCacheManager {
        val configuration = RedisCacheConfiguration
            .defaultCacheConfig()
            .disableCachingNullValues()
            .entryTtl(Duration.ofMinutes(30))
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
                StringRedisSerializer()
            ))

        val config = HashMap<String, RedisCacheConfiguration>()

        config[RedisPolicy.AUTH_KEY] = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(RedisPolicy.AUTH_TTL))
        config[RedisPolicy.PHONE_AUTH_KEY] = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(RedisPolicy.PHONE_AUTH_TTL))

        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory())
            .cacheDefaults(configuration)
            .withInitialCacheConfigurations(config)
            .build()
    }
}