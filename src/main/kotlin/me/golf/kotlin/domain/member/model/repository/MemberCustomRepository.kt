package me.golf.kotlin.domain.member.model.repository

import me.golf.kotlin.domain.member.dto.response.MemberApiShortResponseDto
import me.golf.kotlin.domain.member.dto.request.MemberSearchRequestDto
import me.golf.kotlin.global.security.CustomUserDetails
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional

interface MemberCustomRepository {

    fun getDetailById(memberId: Long): Optional<CustomUserDetails>

    fun findAllBySearchDto(requestDto: MemberSearchRequestDto, pageable: Pageable): Page<MemberApiShortResponseDto>
}