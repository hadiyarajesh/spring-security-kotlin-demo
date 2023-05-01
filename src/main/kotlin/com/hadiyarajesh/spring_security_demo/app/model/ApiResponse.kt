package com.hadiyarajesh.spring_security_demo.app.model

sealed class ApiResponse<T> {
    abstract val status: Boolean

    data class Success<T>(override val status: Boolean, val data: T) : ApiResponse<T>()

    data class Error(override val status: Boolean, val message: String?) : ApiResponse<Nothing>()
}
