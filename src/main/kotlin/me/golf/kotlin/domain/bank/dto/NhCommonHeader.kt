package me.golf.kotlin.domain.bank.dto

data class NhCommonHeader(

    val ApiNm: String,
    val Tsymd: String,
    val Trtm: String,
    val Iscd: String,
    val FintechApsno: String,
    val APISvcCd: String,
    val Istuno: String,
    val AccessToken: String
)