package me.golf.kotlin.domain.member.sms.repository

import me.golf.kotlin.domain.member.sms.model.AuthNumber
import org.springframework.data.repository.CrudRepository

interface AuthNumberRepository: CrudRepository<AuthNumber, String>