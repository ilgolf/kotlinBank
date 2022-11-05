package me.golf.kotlin.global.exception

import me.golf.kotlin.global.exception.error.ErrorCode
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.util.stream.Collectors


class ErrorResponse(
    private val message: String,
    private val status: Int,
    private val errors: List<FieldCustomError>
) {

    companion object {
        fun of(code: ErrorCode, errors: List<FieldCustomError>) = ErrorResponse(code.message, code.status, errors)

        fun of(code: ErrorCode) = ErrorResponse(code.message, code.status, emptyList())

        fun of(code: ErrorCode, bindingResult: BindingResult) =
            ErrorResponse(code.message, code.status, FieldCustomError.of(bindingResult))

        fun of(e: MethodArgumentTypeMismatchException): ErrorResponse {
            val value = e.value.toString()
            val errors: List<FieldCustomError> = FieldCustomError.of(e.name, value, e.errorCode)
            val errorCode = ErrorCode.INVALID_TYPE_VALUE
            return ErrorResponse(errorCode.message, errorCode.status, errors)
        }
    }

    class FieldCustomError(
        private val field: String,
        private val value: String,
        private val reason: String
    ) {

        companion object {
            fun of(bindingResult: BindingResult) = bindingResult.fieldErrors.stream()
                .map { error ->
                    FieldCustomError(
                        error.field,
                        error.rejectedValue.toString(),
                        error?.defaultMessage ?: ""
                    )
                }
                .collect(Collectors.toList())

            fun of(field: String, value: String, reason: String): List<FieldCustomError> {
                val fieldErrors: MutableList<FieldCustomError> = ArrayList()
                fieldErrors.add(FieldCustomError(field, value, reason))
                return fieldErrors
            }
        }
    }
}