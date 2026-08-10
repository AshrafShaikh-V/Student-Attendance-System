package com.attendance.attendance_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS configuration.
 *
 * Restrict allowed origins to your actual front-end domain(s) in production
 * by setting the CORS_ALLOWED_ORIGINS environment variable.
 * Example: CORS_ALLOWED_ORIGINS=https://your-frontend.com
 *
 * Defaults to localhost:3000 for local development.
 */
@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                String allowedOrigins = System.getenv().getOrDefault(
                    "CORS_ALLOWED_ORIGINS", "http://localhost:3000"
                );
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins.split(","))
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("Content-Type", "Authorization", "Accept")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }
}
