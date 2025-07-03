package com.uth.quizclear.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.uth.quizclear.service.CustomUserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.uth.quizclear.interceptor.ScopeInterceptor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        // Không cảnh báo bảo mật khi dùng NoOpPasswordEncoder (chỉ dùng cho demo/bài tập)
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ScopeInterceptor scopeInterceptor) throws Exception {
        http
                .csrf(csrf -> csrf.disable())                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/Static/**", "/css/**", "/js/**", "/img/**", "/fonts/**").permitAll()
                        .requestMatchers("/", "/login", "/logout", "/error").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/health", "/actuator/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/dashboard/**").authenticated()
                        .requestMatchers("/lecturer/**").authenticated()
                        .requestMatchers("/hed-dashboard", "/staff-dashboard", "/sl-dashboard", "/lecturer-dashboard", "/hoe-dashboard").authenticated()
                        .requestMatchers("/test-login-**", "/bypass-login-**", "/debug-session").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl("/login?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/error/403")
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (request.getRequestURI().startsWith("/api/")) {
                                response.sendError(401, "Unauthorized");
                            } else {
                                response.sendRedirect("/login");
                            }
                        })
                )
                .addFilterBefore(scopeInterceptor, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        return builder.build();
    }

    /**
     * Configure scope-based authorization rules
     */
    @Bean
    public ScopeAuthorizationManager scopeAuthorizationManager() {
        return new ScopeAuthorizationManager();
    }

    /**
     * Custom authorization manager for scope-based access control
     */
    public static class ScopeAuthorizationManager {
        
        /**
         * Check if user has access to specific department
         */
        public boolean hasDepartmentAccess(String username, Long departmentId) {
            // Implementation will be handled by ScopeInterceptor
            return true; // Placeholder
        }
        
        /**
         * Check if user has access to specific subject
         */
        public boolean hasSubjectAccess(String username, Long subjectId) {
            // Implementation will be handled by ScopeInterceptor
            return true; // Placeholder
        }
        
        /**
         * Check if user can access specific resource based on role and scope
         */
        public boolean hasResourceAccess(String username, String resourceType, Long resourceId) {
            // Implementation will be handled by ScopeInterceptor
            return true; // Placeholder
        }
    }
}
