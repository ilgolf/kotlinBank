package me.golf.kotlin.commonutil

import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import javax.annotation.PreDestroy


abstract class TestContainer {

    @PreDestroy
    fun stop() {
        REDIS_CONTAINER.stop()
    }

    companion object {
        private const val REDIS_IMAGE = "redis:latest"

        @Container
        @JvmStatic
        val REDIS_CONTAINER: GenericContainer<*> = GenericContainer<Nothing>(REDIS_IMAGE)
            .apply { withExposedPorts(6379) }
            .apply { withReuse(true) }
            .apply { start() }
    }
}