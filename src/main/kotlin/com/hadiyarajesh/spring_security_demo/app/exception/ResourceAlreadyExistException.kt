package com.hadiyarajesh.spring_security_demo.app.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(value = HttpStatus.CONFLICT)
data class ResourceAlreadyExistException(
    val msg: String?,
    val httpStatus: HttpStatus = HttpStatus.CONFLICT
) : RuntimeException(msg)