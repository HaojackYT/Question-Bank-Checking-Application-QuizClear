package com.uth.quizclear.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    // Get user profile by userId
    public Optional<UserBasicDTO> getProfileByUserId(Long userId) {
        return userRepository.findUserBasicDTOByUserId(userId);
    }
}
//Chứa logic nghiệp vụ, xử lý dữ liệu, gọi repository để thao tác DB