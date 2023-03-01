package me.golf.kotlin.global.jwt.dto

data class TokenBaseDto(
    var accessToken: String,
    var refreshToken: String
)