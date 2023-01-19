package me.golf.kotlin.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration

@Configuration
class WebClientConfig {

    @Bean
    fun httpClient(): HttpClient {
        val provider = ConnectionProvider.builder("nh-api")
            .maxConnections(100)
            .pendingAcquireMaxCount(-1)
            .pendingAcquireTimeout(Duration.ofSeconds(45))
            .maxIdleTime(Duration.ofSeconds(10))
            .maxLifeTime(Duration.ofSeconds(10))
            .build()

        return HttpClient.create(provider)
    }

    @Bean
    fun webClient(): WebClient {

        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient()))
            .defaultHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Safari/537.3")
            .build()
    }
}