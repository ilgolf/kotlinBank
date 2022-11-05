package me.golf.kotlin.domain.member.error

class DuplicateEmailException(email: String) : RuntimeException("이메일이 중복됩니다. $email")