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
        Optional<User> userOpt = userRepository.findById(userId);        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserBasicDTO dto = new UserBasicDTO(
                user.getUserId() != null ? user.getUserId().longValue() : null,
                user.getFullName(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().name() : null
            );            // Set additional fields
            dto.setStatus(user.getStatus());
            // Set temporary default values for workPlace and qualification
            // These should be retrieved from User entity when database is updated
            dto.setWorkPlace("Default Workplace"); // TODO: Get from user entity
            dto.setQualification("Default Qualification"); // TODO: Get from user entity
            
            // Set start and end dates (these fields exist in User entity)
            dto.setStart(user.getStart());
            dto.setEnd(user.getEnd());
            
            // Set other fields that exist in User entity
            dto.setGender(user.getGender());
            dto.setDateOfBirth(user.getDateOfBirth());
            dto.setNation(user.getNation());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setHometown(user.getHometown());
            dto.setContactAddress(user.getContactAddress());
            
            return Optional.of(dto);
        }
        return Optional.empty();
    }
}
//Chứa logic nghiệp vụ, xử lý dữ liệu, gọi repository để thao tác DB