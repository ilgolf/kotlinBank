package me.golf.kotlin.domain.member.error

class DuplicateNicknameException(nickname: String) : RuntimeException("닉네임이 중복됩니다. $nickname")
