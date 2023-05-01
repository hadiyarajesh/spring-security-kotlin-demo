package com.hadiyarajesh.spring_security_demo.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.hadiyarajesh.spring_security_demo.app.utility.EndPoint
import com.hadiyarajesh.spring_security_demo.model.RoleName
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.time.LocalDateTime

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val securityFilter: SecurityFilter,
    private val exceptionHandlerFilter: ExceptionHandlerFilter
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http
//            .csrf { csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).disable() }
            .csrf()
            .disable() // Why to disable CSRF? Read here -> https://docs.spring.io/spring-security/reference/features/exploits/csrf.html#csrf-when
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .requestMatchers(PathRequest.toH2Console()).permitAll()
            .requestMatchers(EndPoint.AUTH_ROOT_PATH + EndPoint.SIGN_UP, EndPoint.AUTH_ROOT_PATH + EndPoint.SIGN_IN)
            .permitAll()
            .requestMatchers("${EndPoint.ADMIN_ROOT_PATH}/**").hasAuthority(RoleName.ADMIN.name)
            .anyRequest().authenticated()

        http
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(exceptionHandlerFilter, SecurityFilter::class.java)
            .exceptionHandling { configurer ->
                configurer.authenticationEntryPoint { request, response, authException ->
                    val jsonResponse = mapOf(
                        "status" to false,
                        "message" to "Access denied",
                        "timestamp" to LocalDateTime.now().toString()
                    )

                    response.contentType = MediaType.APPLICATION_JSON_VALUE
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.writer.write(
                        ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonResponse)
                    )
                }
            }

        return http.build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager? {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}
