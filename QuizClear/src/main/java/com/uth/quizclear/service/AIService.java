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

@Service
public class AIService {
    
    @Value("${ai.service.url:http://localhost:5000}")
    private String aiServiceUrl;
    
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public AIService() {
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Check for duplicate questions using AI service
     */
    public Map<String, Object> checkDuplicate(String newQuestion, List<Map<String, Object>> existingQuestions) {
        try {
            // Prepare request payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("new_question", newQuestion);
            payload.put("existing_questions", existingQuestions);
            
            String jsonPayload = objectMapper.writeValueAsString(payload);
            
            // Create HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(aiServiceUrl + "/api/check-duplicate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .timeout(Duration.ofSeconds(30))
                .build();
            
            // Send request
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
              if (response.statusCode() == 200) {
                TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
                return objectMapper.readValue(response.body(), typeRef);
            } else {
                throw new RuntimeException("AI Service error: " + response.statusCode() + " - " + response.body());
            }
            
        } catch (Exception e) {
            // Return fallback response if AI service fails
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("error", "AI service temporarily unavailable: " + e.getMessage());
            fallback.put("duplicates_found", 0);
            fallback.put("similar_questions", List.of());
            return fallback;
        }
    }
    
    /**
     * Health check for AI service
     */
    public boolean isAIServiceHealthy() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(aiServiceUrl + "/health"))
                .GET()
                .timeout(Duration.ofSeconds(5))
                .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
            
        } catch (Exception e) {
            return false;
        }
    }
}
