package com.jtm.server.core.domain.model

import com.fasterxml.jackson.annotation.JsonProperty

data class PageSupport<T>(val content: MutableList<T>, val pageNumber: Int, val pageSize: Int, val totalElements: Int) {

    @JsonProperty
    fun totalPages(): Int {
        return if (pageSize > 0) kotlin.math.ceil((totalElements / pageSize).toDouble()).toInt() else 0
    }

    @JsonProperty
    fun first(): Boolean {
        return pageNumber == 1
    }

    @JsonProperty
    fun last(): Boolean {
        return (pageNumber + 1) * pageSize >= totalElements
    }
}
