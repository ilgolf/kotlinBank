package me.golf.kotlin.domain.member.model.repository

import me.golf.kotlin.global.security.CustomUserDetails
import java.util.Optional

interface MemberCustomRepository {

    fun getDetailById(memberId: Long): Optional<CustomUserDetails>
}