package me.golf.kotlin.global.config

import io.netty.channel.ChannelOption
import io.netty.handler.logging.LogLevel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import reactor.netty.transport.logging.AdvancedByteBufFormat
import java.time.Duration

@Configuration
class WebClientConfig {


    /**
     * 초당 1000개 요청 기준 MAX_CONNECTION 설정 (서버 2대)
     */
    @Bean
    fun httpClient(): HttpClient {
        val provider = ConnectionProvider.builder("nh-api")
            .maxConnections(150)
            .pendingAcquireMaxCount(500)
            .pendingAcquireTimeout(Duration.ofSeconds(45))
            .maxIdleTime(Duration.ofSeconds(10))
            .maxLifeTime(Duration.ofSeconds(10))
            .build()

        return HttpClient.create(provider)
            .wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
            .responseTimeout(Duration.ofSeconds(5))
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
    }

    @Bean
    fun webClient() =
        WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient()))
            .filter(LoggingRequestFilter())
            .build()
}