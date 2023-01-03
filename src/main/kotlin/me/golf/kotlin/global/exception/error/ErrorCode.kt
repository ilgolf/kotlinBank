package me.golf.kotlin.global.exception.error

enum class ErrorCode(val message: String, val status: Int) {

    // common
    INVALID_INPUT_VALUE("올바르지 않은 값입니다.", 400),
    METHOD_NOT_ALLOWED("올바르지 않은 요청 메서드입니다.", 405),
    INTERNAL_SERVER_ERROR("치명적인 서버 오류입니다.", 500),
    HANDLE_ACCESS_DENIED("해당권한으로는 접근할 수 없습니다", 403),
    INVALID_TYPE_VALUE("해당 값은 들어올 수 없습니다. 값을 확인해주세요", 400),

    // member
    PHONE_ACCESS_DENIED("핸드폰 인증을 하지 않으셨습니다.", 400),
    MEMBER_NOT_FOUND("회원을 찾지 못하였습니다.", 400),
    DUPLICATE_NICKNAME("닉네임이 중복됩니다.", 400),
    DUPLICATE_EMAIL("이메일이 중복됩니다.", 400),
    PASSWORD_CONFIRM_FAIL("비밀번호 확인 다시 해주기 바랍니다.", 400),

    // sms
    SEND_FAIL_ERROR("문자 전송에 실패했습니다.", 400),
    SECURE_NUMBER_NOT_FOUND("인증번호를 찾을 수 없습니다. 다시 시도해주세요", 400),
    INVALID_REFRESH_TOKEN("올바르지 않은 토큰이 들어왔습니다. 다시 로그인 바랍니다.", 401),

    // bank
    INVALID_TO_AND_FROM_SAME("송금할 대상 회원과 송금 회원이 같을 수 없습니다.", 400),
    TOO_MUCH_AMOUNT("송금할 금액이 잔고보다 크면 안됩니다.", 400),
}
