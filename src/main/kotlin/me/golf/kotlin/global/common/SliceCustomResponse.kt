package me.golf.kotlin.global.common

import org.springframework.data.domain.Slice

class SliceCustomResponse<T>(
    val contents: List<T>,
    val pageSize: Int,
    val pageNumber: Int
) {

    companion object {
        fun <T> of(response: Slice<T>) =
            SliceCustomResponse(
                contents =  response.content,
                pageSize = response.size,
                pageNumber = response.number
            )
    }
}
