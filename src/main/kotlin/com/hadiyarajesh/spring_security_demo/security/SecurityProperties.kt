package com.hadiyarajesh.spring_security_demo.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.security")
data class SecurityProperties(
    val secret: String,
    val expiration: Long
)
