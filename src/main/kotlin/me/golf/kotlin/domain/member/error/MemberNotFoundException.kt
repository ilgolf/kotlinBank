package me.golf.kotlin.domain.member.error

class MemberNotFoundException(memberId: Long) : RuntimeException("회원을 찾을 수 없습니다. $memberId")
