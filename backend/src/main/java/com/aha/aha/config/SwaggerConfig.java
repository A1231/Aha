package com.aha.aha.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info=@Info(title="API Docs", version="v1")
)

@Configuration
public class SwaggerConfig {
}
