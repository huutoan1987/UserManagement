package com.example.usermanagement.data.model.local

import com.example.usermanagement.interfaces.IRequest

data class UserSearchRequest(
    val searchStr: String = "",
    val page: Int = 1
) : IRequest {
    override
    fun isValid(): Boolean {
        // TODO: For simple, just check number of character below 10
        return searchStr.length < 10
    }

    override
    fun isFirstPage() = (page == 1)

    override
    fun buildRequest(): String {
        return "$searchStr"
    }
}
