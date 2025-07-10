package com.uth.quizclear.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Service để gọi AI service phát hiện câu hỏi trùng lặp
 * Sử dụng HTTP client để gửi request đến Python AI service
 */
@Service
public class AIService {
    
    // URL của AI service (mặc định localhost:5000)
    @Value("${ai.service.url:http://localhost:5000}")
    private String aiServiceUrl;
    
    private final HttpClient httpClient;  // Client để gửi HTTP request
    private final ObjectMapper objectMapper;  // Để chuyển đổi JSON
    
    public AIService() {
        // Khởi tạo HTTP client với timeout 10 giây
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Kiểm tra câu hỏi trùng lặp sử dụng AI service
     * 
     * @param newQuestion Câu hỏi mới cần kiểm tra
     * @param existingQuestions Danh sách câu hỏi đã có
     * @return Kết quả phát hiện trùng lặp từ AI
     */    public Map<String, Object> checkDuplicate(String newQuestion, List<Map<String, Object>> existingQuestions) {
        try {
            // Chuẩn bị dữ liệu gửi đến AI service
            Map<String, Object> payload = new HashMap<>();
            payload.put("new_question", newQuestion);
            payload.put("existing_questions", existingQuestions);
            
            // Chuyển đổi thành JSON string
            String jsonPayload = objectMapper.writeValueAsString(payload);
            
            // Tạo HTTP POST request đến AI service
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(aiServiceUrl + "/api/check-duplicate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .timeout(Duration.ofSeconds(30))  // Timeout 30 giây
                .build();
            
            // Gửi request và nhận response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
              
            // Kiểm tra response thành công (status 200)
            if (response.statusCode() == 200) {
                // Chuyển đổi JSON response thành Map
                TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
                return objectMapper.readValue(response.body(), typeRef);
            } else {
                throw new RuntimeException("AI Service error: " + response.statusCode() + " - " + response.body());
            }
            
        } catch (Exception e) {
            // Trả về response dự phòng nếu AI service lỗi
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("error", "AI service temporarily unavailable: " + e.getMessage());
            fallback.put("duplicates_found", 0);
            fallback.put("similar_questions", List.of());
            return fallback;
        }
    }    
    /**
     * Kiểm tra tình trạng sức khỏe của AI service
     * 
     * @return true nếu AI service hoạt động bình thường, false nếu lỗi
     */
    public boolean isAIServiceHealthy() {
        try {
            // Gửi GET request đến endpoint /health
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(aiServiceUrl + "/health"))
                .GET()
                .timeout(Duration.ofSeconds(5))  // Timeout ngắn hơn cho health check
                .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;  // Trả về true nếu status 200
            
        } catch (Exception e) {
            return false;  // Trả về false nếu có lỗi
        }
    }
}
