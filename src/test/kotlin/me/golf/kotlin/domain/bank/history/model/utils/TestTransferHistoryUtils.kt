package me.golf.kotlin.domain.bank.history.model.utils

import me.golf.kotlin.domain.bank.history.model.TransferHistory
import me.golf.kotlin.domain.member.model.Birthday
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.ProfileImage
import me.golf.kotlin.domain.member.model.UserEmail
import me.golf.kotlin.domain.member.util.GivenMember
import java.math.BigDecimal
import java.time.LocalDate

object TestTransferHistoryUtils {

    val transferMoney = BigDecimal.valueOf(5000)
    const val fromMemberId = 1L
    const val toMemberId = 2L
    val fromMember = GivenMember.toMember()

    fun toEntity(): TransferHistory {
        return TransferHistory(
            transferMoney,
            fromMemberId,
            toMemberId,
            fromMember,
            getToMember()
        )
    }

    fun getToMember(): Member {
        return Member(
            email = UserEmail("nokt@gmail.co.kr"),
            password = "108378127",
            name = "김딱구",
            nickname = "딱구형",
            birth = Birthday(LocalDate.of(1997, 12, 11)),
            profileImage = ProfileImage("/fake/path/baseImage.jpeg"),
            phoneNumber = "010-1234-7878"
        )
    }
}