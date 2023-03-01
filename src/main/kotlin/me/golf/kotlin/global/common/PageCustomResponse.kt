package me.golf.kotlin.global.common

import org.springframework.data.domain.Page
import java.util.*
import kotlin.collections.ArrayList


data class PageCustomResponse<T>(
    val data: MutableList<T> = ArrayList(),
    val totalPage: Int = 0,
    val pageSize: Int = 0,
    val totalElements: Long = 0,
    val number: Int = 0
) {

    companion object {
        fun <T> of(response: Page<T>): PageCustomResponse<T> {
            return PageCustomResponse(
                response.content,
                response.totalPages,
                response.size,
                response.totalElements,
                response.number
            )
        }

        fun <T> emptyPage(): PageCustomResponse<T> {
            return PageCustomResponse(
                Collections.emptyList(),
                0,
                0,
                0,
                0
            )
        }
    }
}