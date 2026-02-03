package com.challenge.bank.configuration

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun customOpenAPI(): OpenAPI {
        val info = Info()
            .title("My Fantastic API")
            .description("Challenge back-API")
            .version("1.0.0")

        val basicScheme = SecurityScheme()
            .type(HTTP)
            .scheme("basic")

        val components = Components()
            .addSecuritySchemes("basicAuth", basicScheme)

        val securityRequirement = SecurityRequirement()
            .addList("basicAuth")

        return OpenAPI()
            .components(components)
            .addSecurityItem(securityRequirement)
            .info(info)
    }
}