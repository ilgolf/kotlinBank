package me.golf.kotlin.domain.bank.client

import me.golf.kotlin.domain.bank.utils.NhHeaderKeyUtils
import me.golf.kotlin.domain.bank.utils.NhHeaderValueUtils
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class NHApiClientImpl(
    private val webClient: WebClient
): NHApiClient {

    override fun getFinAccountConnection(agencyDealCode: String): Mono<String> {

        val nowDateTimeParse = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd hhmmss"))
            .split(" ")

        return webClient
            .get()
            .uri(URI(NhHeaderValueUtils.FIN_ACCOUNT_URL))
            .header(NhHeaderKeyUtils.GET_API_NAME_KEY, NhHeaderValueUtils.GET_API_NAME_VALUE)
            .header(NhHeaderKeyUtils.TRANSFER_DATE_KEY, nowDateTimeParse[0])
            .header(NhHeaderKeyUtils.TRANSFER_TIME_KEY, nowDateTimeParse[1])
            .header(NhHeaderKeyUtils.AGENCY_CODE_KEY, NhHeaderValueUtils.AGENCY_CODE_VALUE)
            .header(NhHeaderKeyUtils.FINTECH_NUMBER_KEY, NhHeaderValueUtils.FINTECH_NUMBER_VALUE)
            .header(NhHeaderKeyUtils.SERVICE_CODE_KEY, NhHeaderValueUtils.SERVICE_CODE_VALUE)
            .header(NhHeaderKeyUtils.AGENCY_DEAL_CODE_KEY, agencyDealCode)
            .header(NhHeaderKeyUtils.ACCESS_TOKEN_KEY, NhHeaderValueUtils.ACCESS_TOKEN_VALUE)
            .retrieve()
            .bodyToMono(String::class.java)
    }
}