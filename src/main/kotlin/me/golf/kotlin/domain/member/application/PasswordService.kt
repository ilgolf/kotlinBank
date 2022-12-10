package me.golf.kotlin.domain.member.application

import me.golf.kotlin.domain.member.dto.TempPasswordApiResponseDto
import me.golf.kotlin.domain.member.error.MemberNotFoundException
import me.golf.kotlin.domain.member.model.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PasswordService(
    private val memberRepository: MemberRepository,
    private val encoder: PasswordEncoder
) {

    fun updatePassword(password: String, memberId: Long) {
        val member = memberRepository.findByIdOrNull(memberId)?: throw MemberNotFoundException(memberId)
        member.changePassword(encoder, password)
    }

    fun publishTempPassword(memberId: Long): TempPasswordApiResponseDto {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw MemberNotFoundException(memberId)

        val randomUUID = UUID.randomUUID()
        val uuid = randomUUID.toString().split("-")
        val tempPassword = uuid[0]

        member.changePassword(encoder, tempPassword)

        return TempPasswordApiResponseDto(tempPassword)
    }
}
