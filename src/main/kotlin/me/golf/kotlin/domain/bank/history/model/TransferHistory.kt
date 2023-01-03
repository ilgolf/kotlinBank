package me.golf.kotlin.domain.bank.history.model

import lombok.AccessLevel
import lombok.NoArgsConstructor
import me.golf.kotlin.domain.bank.history.error.InvalidByToAndFromSameException
import me.golf.kotlin.domain.bank.model.BankAccount
import me.golf.kotlin.domain.member.model.Member
import me.golf.kotlin.global.common.BaseTimeEntity
import java.math.BigDecimal
import javax.persistence.*

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class TransferHistory(
    @Embedded
    var transferMoney: BigDecimal,

    @Column(name = "from_member_id", nullable = false)
    var fromMemberId: Long,

    @Column(name = "to_member_id", nullable = false)
    var toMemberId: Long,

    @Column(name = "bank_id", nullable = false)
    var bankId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false, insertable = false, updatable = false)
    var bankAccount: BankAccount,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id", updatable = false, insertable = false)
    var from: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id", updatable = false, insertable = false)
    var to: Member
): BaseTimeEntity() {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_history_id", updatable = false, nullable = false)
    var id: Long = 0

    fun validToAndFromSame(to: Member, from: Member) {
        if (to == from) {
            throw InvalidByToAndFromSameException()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TransferHistory

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}