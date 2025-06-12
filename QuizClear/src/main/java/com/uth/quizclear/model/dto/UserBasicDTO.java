package com.uth.quizclear.model.dto;

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
}
