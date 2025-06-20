package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<UserBasicDTO> getProfileByUserId(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserBasicDTO dto = new UserBasicDTO(
                user.getUserId() != null ? user.getUserId().longValue() : null,
                user.getFullName(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().name() : null
            );
            dto.setStatus(user.getStatus());
            dto.setWorkPlace("Default Workplace");
            dto.setQualification("Default Qualification");
            dto.setStart(user.getStart());
            dto.setEnd(user.getEnd());
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

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUser(User user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }
}