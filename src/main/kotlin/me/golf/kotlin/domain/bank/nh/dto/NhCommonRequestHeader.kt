package me.golf.kotlin.domain.bank.nh.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class NhCommonRequestHeader(


    @field:JsonProperty("ApiNm")
    val apiNm: String,

    @field:JsonProperty("Tsymd")
    val tsymd: String,

    @field:JsonProperty("Trtm")
    val trtm: String,

    @field:JsonProperty("Iscd")
    val iscd: String,

    @field:JsonProperty("FintechApsno")
    val fintechApsno: String,

    @field:JsonProperty("ApiSvcCd")
    val apiSvcCd: String,

    @field:JsonProperty("IsTuno")
    val istuno: String,

    @field:JsonProperty("AccessToken")
    val accessToken: String
)