package me.golf.kotlin.domain.member.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import me.golf.kotlin.domain.member.model.Birthday
import me.golf.kotlin.domain.member.model.ProfileImage
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class MemberApiUpdateRequestDto(

    @field:NotBlank(message = "필수 값입니다.")
    @JsonProperty("name")
    var name: String,

    @field:NotBlank(message = "필수 값입니다.")
    @JsonProperty("nickname")
    var nickname: String,

    @field:NotNull(message = "필수 값입니다.")
    @JsonProperty("profileImage")
    var profileImage: String,

    @field:NotNull(message = "필수 값입니다.")
    @JsonProperty("birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var birth: LocalDate
) {

    fun toService() = MemberUpdateRequestDto(name, nickname, ProfileImage(profileImage), Birthday(birth))
}