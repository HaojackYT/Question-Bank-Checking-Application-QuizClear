package com.uth.quizclear.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**") // Áp dụng cho tất cả các endpoint API của bạn
                .allowedOrigins("http://127.0.0.1:5500") // CHỈ ĐỊNH RÕ NGUỒN GỐC CỦA FRONTEND CỦA BẠN
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các phương thức HTTP được phép
                .allowedHeaders("*") // Cho phép tất cả các header
                .allowCredentials(true); // Quan trọng nếu bạn dùng cookie/session hoặc thông tin xác thực khác
    }
      @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Static resources mapping (case sensitive) - exclude API paths
        registry.addResourceHandler("/Static/**")
                .addResourceLocations("classpath:/Static/")
                .setCachePeriod(3600);
                
        registry.addResourceHandler("/Template/**")
                .addResourceLocations("classpath:/Template/")
                .setCachePeriod(3600);
        
        // Legacy static mapping for compatibility - exclude API paths
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/Static/");
        
        // Ensure API paths are NOT handled as static resources
        // API requests should go to controllers, not static resources
    }    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
    }
}