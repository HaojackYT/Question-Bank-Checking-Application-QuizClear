package com.uth.quizclear.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**") // Áp dụng cho tất cả các endpoint API của bạn
                .allowedOrigins("http://127.0.0.1:5500") // CHỈ ĐỊNH RÕ NGUỒN GỐC CỦA FRONTEND CỦA BẠN
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các phương thức HTTP được phép
                .allowedHeaders("*") // Cho phép tất cả các header
                .allowCredentials(true); // Quan trọng nếu bạn dùng cookie/session hoặc thông tin xác thực khác
    }    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Static resources mapping (case sensitive) - exclude API paths
        // Disable cache for development (0 = no cache)
        registry.addResourceHandler("/Static/**")
                .addResourceLocations("classpath:/Static/")
                .setCachePeriod(0)
                .resourceChain(false); // Disable resource chain caching
                
        registry.addResourceHandler("/Template/**")
                .addResourceLocations("classpath:/Template/")
                .setCachePeriod(0)
                .resourceChain(false); // Disable resource chain caching
        
        // Legacy static mapping for compatibility - exclude API paths
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/Static/")
                .setCachePeriod(0)
                .resourceChain(false); // Disable resource chain caching
        
        // Ensure API paths are NOT handled as static resources
        // API requests should go to controllers, not static resources
    }@Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
    }
    // Add interceptor to set no-cache headers for static resources
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
                String requestPath = request.getRequestURI();
                // Apply no-cache headers to static resources
                if (requestPath.startsWith("/Static/") || requestPath.startsWith("/static/") || 
                    requestPath.endsWith(".js") || requestPath.endsWith(".css") || 
                    requestPath.endsWith(".png") || requestPath.endsWith(".jpg") || 
                    requestPath.endsWith(".jpeg") || requestPath.endsWith(".gif")) {
                    
                    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                    response.setHeader("Pragma", "no-cache");
                    response.setHeader("Expires", "0");
                }
                return true;
            }
        });
    }
}