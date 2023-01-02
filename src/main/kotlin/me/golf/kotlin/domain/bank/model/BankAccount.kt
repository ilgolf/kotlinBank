package me.golf.kotlin.domain.bank.model

import lombok.AccessLevel
import lombok.NoArgsConstructor
import me.golf.kotlin.domain.member.model.Member
import org.springframework.security.crypto.password.PasswordEncoder
import javax.persistence.*

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Account")
class BankAccount(
    @Column(length = 30, unique = true, nullable = false)
    var number: String,

    @Column(name = "account_password", length = 200, nullable = false)
    var password: String,

    @Column(name = "member_id")
    var memberId: Long,

    @Column(length = 10, nullable = false)
    var bankName: String,

    @Column(name = "account_name")
    var name: String,
) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", unique = true, nullable = false, updatable = false)
    var id: Long = 0L

    @Column(nullable = false)
    var deleted = false

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    lateinit var member: Member

    // 비즈니스 로직
    fun encodePassword(passwordEncoder: PasswordEncoder): BankAccount {
        this.password = passwordEncoder.encode(this.password)
        return this
    }

    fun matchPassword(passwordEncoder: PasswordEncoder, rawPassword: String): BankAccount {
        passwordEncoder.matches(rawPassword, this.password)
        return this
    }

    fun updateAccountName(name: String): BankAccount {
        this.name = name
        return this;
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BankAccount

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}