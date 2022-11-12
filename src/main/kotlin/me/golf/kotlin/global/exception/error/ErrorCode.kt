package me.golf.kotlin.global.exception.error

enum class ErrorCode(val message: String, val status: Int) {

    INVALID_INPUT_VALUE("올바르지 않은 값입니다.", 400),
    METHOD_NOT_ALLOWED("올바르지 않은 요청 메서드입니다.", 405),
    INTERNAL_SERVER_ERROR("치명적인 서버 오류입니다.", 500),
    HANDLE_ACCESS_DENIED("해당권한으로는 접근할 수 없습니다", 403),
    INVALID_TYPE_VALUE("해당 값은 들어올 수 없습니다. 값을 확인해주세요", 400);
}
