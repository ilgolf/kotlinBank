package me.golf.kotlin.domain.bank.client

import reactor.core.publisher.Mono

interface NHApiClient {

    fun getFinAccountConnection(agencyDealCode: String): Mono<String>
}
