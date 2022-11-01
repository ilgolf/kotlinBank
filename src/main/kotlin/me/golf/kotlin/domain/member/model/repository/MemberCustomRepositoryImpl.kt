package me.golf.kotlin.domain.member.model.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import me.golf.kotlin.domain.member.model.QMember
import me.golf.kotlin.global.security.CustomUserDetails
import me.golf.kotlin.global.security.QCustomUserDetails
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
}