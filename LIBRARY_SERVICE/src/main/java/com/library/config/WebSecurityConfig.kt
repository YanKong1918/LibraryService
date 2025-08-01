package com.library.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.filter.CharacterEncodingFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web -> web.ignoring().requestMatchers("/**") }
    }

    @Bean
    @Throws(Exception::class)
    fun setSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val filter = CharacterEncodingFilter()
        filter.encoding = "UTF-8"
        filter.setForceEncoding(true)

        http
            .csrf { csrfConfig -> csrfConfig.disable() }
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests.anyRequest().permitAll()
            }
        return http.build()
    }

}