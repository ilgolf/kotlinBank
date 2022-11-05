package me.golf.kotlin.domain.member.application

import me.golf.kotlin.domain.member.dto.request.MemberSaveRequestDto
import me.golf.kotlin.domain.member.dto.request.MemberUpdateRequestDto
import me.golf.kotlin.domain.member.error.DuplicateEmailException
import me.golf.kotlin.domain.member.error.DuplicateNicknameException
import me.golf.kotlin.domain.member.error.MemberNotFoundException
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.domain.member.model.UserEmail
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
@Service
class MemberCommandService(
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun save(saveDto: MemberSaveRequestDto): Long {
        this.validateDuplicationEmail(saveDto.email)
        this.validateDuplicationNickname(saveDto.nickname)

        return memberRepository.save(saveDto.toMemberEntity()).id
    }

    @Transactional
    fun update(requestDto: MemberUpdateRequestDto, memberId: Long) {
        val member = getMember(memberId)

        if (member.nickname != requestDto.nickname) {
            validateDuplicationNickname(requestDto.nickname)
        }

        member.updateMember(requestDto.nickname, requestDto.name,
            requestDto.birthday.value, requestDto.profileImage.value)
    }

    @Transactional
    fun delete(memberId: Long) {
        getMember(memberId).delete()
    }


    private fun validateDuplicationNickname(nickname: String) {
        if (memberRepository.existsByNickname(nickname)) {
            throw DuplicateNicknameException(nickname)
        }
    }

    private fun validateDuplicationEmail(email: String) {
        if (memberRepository.existsByEmail(UserEmail(email))) {
            throw DuplicateEmailException(email)
        }
    }

    private fun getMember(memberId: Long): Member =
        memberRepository.findById(memberId)
            .orElseThrow { throw MemberNotFoundException(memberId) }
}

