package com.hadiyarajesh.spring_security_demo.app.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(value = HttpStatus.NOT_FOUND)
data class ResourceNotFoundException(
    val msg: String?,
    val httpStatus: HttpStatus = HttpStatus.NOT_FOUND
) : RuntimeException(msg)
