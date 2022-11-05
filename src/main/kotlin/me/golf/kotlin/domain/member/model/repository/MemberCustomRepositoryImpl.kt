package me.golf.kotlin.domain.member.model.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import me.golf.kotlin.domain.member.dto.response.MemberApiShortResponseDto
import me.golf.kotlin.domain.member.dto.request.MemberSearchRequestDto
import me.golf.kotlin.domain.member.dto.response.QMemberApiShortResponseDto
import me.golf.kotlin.domain.member.model.QMember
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

    override fun getDetailById(memberId: Long): Optional<CustomUserDetails> {
        return Optional.ofNullable(
            queryFactory.select(
                QCustomUserDetails(
                    QMember.member.id,
                    QMember.member.email,
                    QMember.member.roleType))
                .from(QMember.member)
                .where(QMember.member.id.eq(memberId))
                .fetchFirst()
        )
    }

    override fun findAllBySearchDto(
        requestDto: MemberSearchRequestDto,
        pageable: Pageable
    ): Page<MemberApiShortResponseDto> {

        val data = queryFactory.select(
            QMemberApiShortResponseDto(
                QMember.member.email,
                QMember.member.name,
                QMember.member.profileImage)
        )
            .from(QMember.member)
            .where(
                eqEmail(requestDto.email),
                eqNickname(requestDto.nickname))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(QMember.member.id.desc())
            .fetch()

        if (data.size == 0) {
            return Page.empty()
        }

        val count = queryFactory.select(QMember.member.count())
            .from(QMember.member)
            .where(
                eqEmail(requestDto.email),
                eqNickname(requestDto.nickname))

        return PageableExecutionUtils.getPage(data, pageable, count::fetchFirst)
    }

    private fun eqNickname(nickname: String?): BooleanExpression? {
        if (nickname == null) {
            return null
        }

        return QMember.member.nickname.like("$nickname%")
    }

    private fun eqEmail(email: String?): BooleanExpression? {
        if (email == null) {
            return null
        }

        return QMember.member.email.value.like("$email%")
    }
}