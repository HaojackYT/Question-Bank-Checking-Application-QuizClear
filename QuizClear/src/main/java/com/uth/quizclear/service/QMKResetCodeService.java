package com.uth.quizclear.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class QMKResetCodeService {
    private final Map<String, ResetCode> codeStore = new HashMap<>();
    private static final int CODE_EXPIRY_MINUTES = 5;

    public static class ResetCode {
        private final String code;
        private final LocalDateTime expiryTime;

        public ResetCode(String code, LocalDateTime expiryTime) {
            this.code = code;
            this.expiryTime = expiryTime;
        }

        public String getCode() {
            return code;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expiryTime);
        }
    }

    public String generateCode(String email) {
        String code = String.format("%06d", new Random().nextInt(1000000));
        codeStore.put(email, new ResetCode(code, LocalDateTime.now().plusMinutes(CODE_EXPIRY_MINUTES)));
        return code;
    }

    public boolean verifyCode(String email, String code) {
        ResetCode resetCode = codeStore.get(email);
        if (resetCode == null || resetCode.isExpired()) {
            codeStore.remove(email);
            return false;
        }
        return resetCode.getCode().equals(code);
    }

    public void removeCode(String email) {
        codeStore.remove(email);
    }
}