package me.golf.kotlin.domain.bank.history.model

import org.springframework.data.jpa.repository.JpaRepository

interface TransferHistoryRepository: JpaRepository<TransferHistory, Long>, TransferHistoryCustomRepository