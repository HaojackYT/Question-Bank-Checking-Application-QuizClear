package com.uth.quizclear.service;

import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.Status;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("=== CustomUserDetailsService.loadUserByUsername ===");
        System.out.println("Email: " + email);
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            System.out.println("User not found with email: " + email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        
        User user = userOpt.get();
        System.out.println("Found user: " + user.getFullName());
        System.out.println("User ID: " + user.getUserId());
        System.out.println("User status: " + user.getStatus());
        System.out.println("User isLocked: " + user.getIsLocked());
        System.out.println("User loginAttempts: " + user.getLoginAttempts());
        System.out.println("User password hash: " + user.getPasswordHash());
        System.out.println("User canLogin(): " + user.canLogin());
        
        if (user.getStatus() != Status.ACTIVE || Boolean.TRUE.equals(user.getIsLocked())) {
            System.out.println("User is not active or is locked: " + email);
            throw new UsernameNotFoundException("User is not active or is locked: " + email);
        }
        
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRole() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        }
        
        System.out.println("User authorities: " + authorities);
        System.out.println("Creating UserDetails for: " + user.getEmail());
        
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                authorities
        );
    }
}
