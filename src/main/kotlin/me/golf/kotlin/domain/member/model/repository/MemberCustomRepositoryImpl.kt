package me.golf.kotlin.domain.member.model.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import me.golf.kotlin.domain.member.dto.response.MemberApiShortResponseDto
import me.golf.kotlin.domain.member.dto.request.MemberSearchRequestDto
import me.golf.kotlin.domain.member.dto.response.QMemberApiShortResponseDto
import me.golf.kotlin.domain.member.model.QMember
import me.golf.kotlin.domain.member.model.QMember.*
import me.golf.kotlin.global.security.CustomUserDetails
import me.golf.kotlin.global.security.QCustomUserDetails
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class MemberCustomRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : MemberCustomRepository {

    override fun getDetailById(memberId: Long): CustomUserDetails? {
        return queryFactory.select(
                QCustomUserDetails(
                    member.id,
                    member.email,
                    member.roleType))
                .from(member)
                .where(member.id.eq(memberId))
                .fetchFirst()
    }

    override fun findAllBySearchDto(
        requestDto: MemberSearchRequestDto,
        pageable: Pageable
    ): Page<MemberApiShortResponseDto> {

        val data = queryFactory.select(
            QMemberApiShortResponseDto(
                member.email,
                member.name,
                member.profileImage)
        )
            .from(member)
            .where(
                eqEmail(requestDto.email),
                eqNickname(requestDto.nickname))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(member.id.desc())
            .fetch()

        if (data.size == 0) {
            return Page.empty()
        }

        val count = queryFactory.select(member.count())
            .from(member)
            .where(
                eqEmail(requestDto.email),
                eqNickname(requestDto.nickname))

        return PageableExecutionUtils.getPage(data, pageable, count::fetchFirst)
    }

    override fun getDetailByEmail(email: String): CustomUserDetails? {
        return queryFactory.select(
            QCustomUserDetails(
                member.id,
                member.email,
                member.roleType
            )
        )
            .from(member)
            .where(member.email.value.eq(email))
            .fetchFirst()
    }

    private fun eqNickname(nickname: String?): BooleanExpression? {
        if (nickname == null) {
            return null
        }

        return member.nickname.like("$nickname%")
    }

    private fun eqEmail(email: String?): BooleanExpression? {
        if (email == null) {
            return null
        }

        return member.email.value.like("$email%")
    }
}