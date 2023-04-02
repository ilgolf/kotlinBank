package me.golf.kotlin.domain.bank.dto

import com.querydsl.core.annotations.QueryProjection

data class FinAccountAndBankIdDto

@QueryProjection
constructor(
    val bankId: Long,
    val finAccount: String
)
