package me.golf.kotlin.domain.member.application

import me.golf.kotlin.domain.member.dto.request.MemberSaveRequestDto
import me.golf.kotlin.domain.member.dto.request.MemberUpdateRequestDto
import me.golf.kotlin.domain.member.error.MemberException
import me.golf.kotlin.domain.member.model.UserEmail
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberCommandService(
    private val memberRepository: MemberRepository,
    private val encoder: PasswordEncoder
) {

    @Transactional
    fun save(saveDto: MemberSaveRequestDto): Long {
        this.validateDuplicationEmail(saveDto.email)
        this.validateDuplicationNickname(saveDto.nickname)

        val member = saveDto.toMemberEntity().encodePassword(encoder)

        return memberRepository.save(member).id
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
        check(!memberRepository.existsByNickname(nickname)) { throw MemberException.DuplicateNicknameException(nickname) }
    }

    private fun validateDuplicationEmail(email: String) {
        check(!memberRepository.existsByEmail(UserEmail(email))) { throw MemberException.DuplicateEmailException(email) }
    }

    private fun getMember(memberId: Long) =
        memberRepository.findByIdOrNull(memberId)?: throw MemberException.MemberNotFoundException(memberId)
}

