package me.golf.kotlin.global.config

import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import reactor.core.publisher.Mono


class LoggingRequestFilter: ExchangeFilterFunction {

    private val logger = LoggerFactory.getLogger(WebClientConfig::class.java)

    override fun filter(request: ClientRequest, next: ExchangeFunction): Mono<ClientResponse> {
        if (request.method() == HttpMethod.POST || request.method() == HttpMethod.PUT) {
            logger.info("Request URI: {}", request.url())
            logger.info("Request headers: {}", request.headers())

            if (request.headers().contentType?.includes(MediaType.APPLICATION_JSON) == true) {
                logger.info("Request body: {}", request.body().toString())
            }
        }
        return next.exchange(request)
    }
}