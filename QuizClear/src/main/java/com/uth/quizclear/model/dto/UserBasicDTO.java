package com.uth.quizclear.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.uth.quizclear.model.enums.Gender;
import com.uth.quizclear.model.enums.Status;

/**
 * DTO chứa thông tin cơ bản của người dùng
 * Được sử dụng khi chỉ cần hiển thị thông tin tóm tắt
 */
public class UserBasicDTO {

    private Long userId;
    private String fullName;
    private String email;
    private String role;
    private String department;
    private String avatarUrl;
    private Gender gender;       // Bổ sung
    private LocalDate dateOfBirth;      // Bổ sung
    private String nation;          // Bổ sung
    private String phoneNumber;         // Bổ sung
    private String hometown;        // Bổ sung
    private String contactAddress;      // Bổ sung
    private LocalDateTime start;        // Bổ sung
    private LocalDateTime end;      // Bổ sung
    private Status status;      // Bổ sung
    private String workPlace;   // Bổ sung trường workPlace cho Profile
    private String qualification;   // Bổ sung trường qualification cho Profile

    // ========== CONSTRUCTORS CŨ CỦA BẠN - GIỮ NGUYÊN ==========

    public UserBasicDTO() {
    }

    public UserBasicDTO(Long userId, String fullName, String email, String role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public UserBasicDTO(Long userId, String fullName, String email, String role,
            String department, String avatarUrl) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.department = department;
        this.avatarUrl = avatarUrl;
    }

    // ========== THÊM CONSTRUCTOR MỚI CHO LOGIN SYSTEM ==========

    /**
     * Constructor tương thích với AuthService (nhận Integer userId)
     */
    public UserBasicDTO(Integer userId, String fullName, String email, String role, String department) {
        this.userId = userId != null ? userId.longValue() : null;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.department = department;
    }    /**
     * Constructor đầy đủ cho query findUserBasicDTOByUserId
     */
    public UserBasicDTO(Long userId, String fullName, String email, com.uth.quizclear.model.enums.UserRole role, String department,
                       Gender gender, LocalDate dateOfBirth, String nation, String phoneNumber,
                       String hometown, String contactAddress, LocalDateTime start, LocalDateTime end,
                       Status status, String workPlace, String qualification) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.role = role != null ? role.name() : null;
        this.department = department;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.nation = nation;
        this.phoneNumber = phoneNumber;
        this.hometown = hometown;
        this.contactAddress = contactAddress;
        this.start = start;
        this.end = end;
        this.status = status;
        this.workPlace = workPlace;
        this.qualification = qualification;
    }

    // ========== GETTERS CŨ CỦA BẠN - GIỮ NGUYÊN ==========

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getDepartment() {
        return department;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    // ========== SETTERS CŨ CỦA BẠN - GIỮ NGUYÊN ==========

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    // ========== UTILITY METHODS CŨ CỦA BẠN - GIỮ NGUYÊN ==========

    /**
     * Lấy tên hiển thị ngắn gọn
     */
    public String getDisplayName() {
        return fullName != null ? fullName : email != null ? email.split("@")[0] : "Unknown";
    }

    /**
     * Lấy tên vai trò đầy đủ
     */
    public String getRoleDisplayName() {
        if (role == null)
            return "Unknown";

        switch (role.toUpperCase()) {
            case "RD":
                return "Research Director";
            case "HOD":
                return "Head of Department";
            case "SL":
                return "Subject Leader";
            case "LEC":
                return "Lecturer";
            case "HOED":
                return "Head of Examination Department";
            default:
                return role;
        }
    }

    /**
     * Kiểm tra có phải là người quản lý không
     */
    public boolean isManager() {
        return role != null && (role.equalsIgnoreCase("RD") ||
                role.equalsIgnoreCase("HOD") ||
                role.equalsIgnoreCase("SL") ||
                role.equalsIgnoreCase("HOED"));
    }

    /**
     * Kiểm tra có avatar không
     */
    public boolean hasAvatar() {
        return avatarUrl != null && !avatarUrl.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "UserBasicDTO{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", department='" + department + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        UserBasicDTO that = (UserBasicDTO) obj;
        return userId != null && userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }

    // ========== THÊM GETTERS VÀ SETTERS CHO CÁC TRƯỜNG MỚI ==========
    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
