package com.uth.quizclear.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // Temporary implementation - Get user profile by userId
    public Optional<UserBasicDTO> getProfileByUserId(Long userId) {
        // Temporarily use findById and manually convert to UserBasicDTO
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserBasicDTO dto = new UserBasicDTO(
                user.getUserId() != null ? user.getUserId().longValue() : null,
                user.getFullName(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().name() : null
            );
            return Optional.of(dto);
        }
        return Optional.empty();
    }
}
//Chứa logic nghiệp vụ, xử lý dữ liệu, gọi repository để thao tác DB