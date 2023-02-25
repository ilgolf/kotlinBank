package me.golf.kotlin.global.config

import org.springframework.data.redis.serializer.RedisSerializer
import java.nio.charset.StandardCharsets


class IntRedisSerializer: RedisSerializer<Int> {

    override fun serialize(value: Int?): ByteArray? {
        return value?.toString()?.toByteArray(StandardCharsets.UTF_8)
    }

    override fun deserialize(bytes: ByteArray?): Int? {
        return if (bytes == null) null else (String(bytes, StandardCharsets.UTF_8).toInt())

    }
}