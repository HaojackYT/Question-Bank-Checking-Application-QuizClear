package com.uth.quizclear.config; // Đặt đúng package của bạn

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Áp dụng cho tất cả các endpoint API của bạn
                .allowedOrigins("http://127.0.0.1:5500") // CHỈ ĐỊNH RÕ NGUỒN GỐC CỦA FRONTEND CỦA BẠN
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các phương thức HTTP được phép
                .allowedHeaders("*") // Cho phép tất cả các header
                .allowCredentials(true); // Quan trọng nếu bạn dùng cookie/session hoặc thông tin xác thực khác
    }
}