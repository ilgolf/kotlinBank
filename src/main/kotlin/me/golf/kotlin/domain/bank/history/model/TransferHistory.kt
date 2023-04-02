package me.golf.kotlin.domain.bank.history.model

import lombok.AccessLevel
import lombok.NoArgsConstructor
import me.golf.kotlin.domain.bank.model.BankAccount
import me.golf.kotlin.global.common.BaseEntity
import me.golf.kotlin.global.common.BaseTimeEntity
import java.math.BigDecimal
import javax.persistence.*

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class TransferHistory(

    @Column(name = "transferMoney", nullable = false)
    var transferMoney: BigDecimal,

    @Column(name = "client_id", nullable = false)
    var client: Long,

    @Column(name = "bank_id", nullable = false)
    var bankId: Long,

    @Column(name = "transfer_status", nullable = false)
    @Enumerated(EnumType.STRING)
    var transferStatus: TransferStatus,

    @Column(name = "result_message")
    var resultMessage: String

): BaseEntity() {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_history_id", updatable = false, nullable = false)
    var id: Long = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false, insertable = false, updatable = false)
    lateinit var bankAccount: BankAccount

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