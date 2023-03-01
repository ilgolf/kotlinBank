package me.golf.kotlin.domain.member.dto.request

data class MemberApiSearchRequestDto(
    val email: String?,
    val nickname: String?
) {
    fun toServiceDto() = MemberSearchRequestDto(email, nickname)
}