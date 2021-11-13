package com.example.usermanagement.interfaces

interface IRequest {
    fun isValid(): Boolean
    fun isFirstPage(): Boolean
    fun buildRequest(): String
}