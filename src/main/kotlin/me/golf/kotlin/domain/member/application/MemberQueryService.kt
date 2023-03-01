package me.golf.kotlin.domain.member.application

import me.golf.kotlin.domain.member.dto.request.MemberSearchRequestDto
import me.golf.kotlin.domain.member.dto.response.MemberApiDetailDto
import me.golf.kotlin.domain.member.dto.response.MemberApiShortResponseDto
import me.golf.kotlin.domain.member.error.MemberException
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import me.golf.kotlin.global.common.PageCustomResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberQueryService(
    private val memberRepository: MemberRepository
) {

    @Transactional(readOnly = true)
    fun getDetail(memberId: Long): MemberApiDetailDto {

        val member =
            memberRepository.findByIdOrNull(memberId) ?: throw MemberException.MemberNotFoundException(memberId)

        return MemberApiDetailDto.of(member)
    }

    @Transactional(readOnly = true)
    fun search(requestDto: MemberSearchRequestDto?, pageable: Pageable): PageCustomResponse<MemberApiShortResponseDto> {

        if (requestDto == null) {
            return PageCustomResponse.emptyPage()
        }

        return PageCustomResponse.of(memberRepository.findAllBySearchDto(requestDto, pageable))
    }
}
