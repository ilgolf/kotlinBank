package me.golf.kotlin.domain.member.model

import me.golf.kotlin.global.common.BaseTimeEntity
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(
    name = "member",
    indexes = [
        Index(name = "i_email", columnList = "email"),
        Index(name = "i_nickname", columnList = "nickname")
    ]
)
class Member (
    @Embedded
    var email: UserEmail,

    @Column(length = 200, nullable = false)
    var password: String,

    @Column(length = 20, nullable = false)
    var name: String,

    @Column(length = 20, unique = true, nullable = false)
    var nickname: String,

    @Embedded
    var birth: Birthday,

    @Embedded
    var profileImage: ProfileImage,

    @Column(length = 20, nullable = false)
    var phoneNumber: String): BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false, updatable = false)
    var id: Long = 0

    @Column(nullable = false)
    var deleted: Boolean = false

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var roleType: RoleType = RoleType.USER

    // 비즈니스 로직
    fun updateMember(nickname: String, name: String, birth: LocalDate, profileImage: String): Member {
        this.nickname = nickname
        this.name = name
        this.birth = Birthday(birth)
        this.profileImage = ProfileImage(profileImage)

        return this
    }

    fun matchPassword(passwordEncoder: PasswordEncoder, rawPassword: String): Boolean {
        if (passwordEncoder.matches(rawPassword, this.password)) {
            return true
        }

        return false
    }

    fun encodePassword(passwordEncoder: PasswordEncoder): Member {
        this.password = passwordEncoder.encode(this.password)
        return this
    }

    fun getEmailId(): String {
        return this.email.getId()
    }

    fun validateExceedDate(birth: LocalDate): Boolean {
        return this.birth.validateExceedNowYear(birth)
    }

    fun getAge(): Int {
        return this.birth.getAge()
    }

    fun delete(): Member {
        this.deleted = true
        return this
    }

    fun changePassword(passwordEncoder: PasswordEncoder, password: String): Member {
        this.password = passwordEncoder.encode(password)
        return this
    }

    fun validationProfileUrl(profileImage: ProfileImage): Boolean {
        return this.profileImage.validateImagePath(profileImage)
    }

    // equalsAndHashcode
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
