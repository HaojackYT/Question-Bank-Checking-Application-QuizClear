package com.uth.quizclear.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;

/**
 * Configuration class for AI service integration and web settings
 */
@Configuration
public class AiServiceConfig {

    @Value("${ai.service.url}")
    private String aiServiceUrl;
    
    @Value("${ai.service.timeout}")
    private int timeout;

    /**
     * Configure RestTemplate for AI service communication
     */
    @Bean(name = "aiServiceRestTemplate")
    public RestTemplate aiServiceRestTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri(aiServiceUrl)
                .setConnectTimeout(Duration.ofMillis(timeout))
                .setReadTimeout(Duration.ofMillis(timeout))
                .build();
    }

    /**
     * General RestTemplate bean
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * CORS configuration
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:8081", "http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
