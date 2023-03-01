package me.golf.kotlin.domain.bank.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class NhCommonHeader(


    @field:JsonProperty("ApiNm")
    val ApiNm: String,

    @field:JsonProperty("Tsymd")
    val Tsymd: String,

    @field:JsonProperty("Trtm")
    val Trtm: String,

    @field:JsonProperty("Iscd")
    val Iscd: String,

    @field:JsonProperty("FintechApsno")
    val FintechApsno: String,

    @field:JsonProperty("ApiSvcCd")
    val APISvcCd: String,

    @field:JsonProperty("IsTuno")
    val Istuno: String,

    @field:JsonProperty("AccessToken")
    val AccessToken: String
)