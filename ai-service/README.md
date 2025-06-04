# AI Service cho QuizClear

## Chức năng
- Kiểm tra trùng lặp câu hỏi bằng AI (Sentence Transformers)
- API endpoint cho Spring Boot integration
- Xử lý batch câu hỏi và tính toán độ tương đồng

## Cấu trúc thư mục

```
ai-service/
├── src/                  # Mã nguồn chính
│   ├── app.py            # Flask API server
│   ├── config.py         # Cấu hình hệ thống
│   └── duplicate_detector.py  # Logic AI
├── tests/                # Unit tests
│   └── test_api.py       # API test script
├── models/               # Thư mục lưu cache AI model
├── .env                  # Environment variables
├── requirements.txt      # Dependencies
├── startup.py            # Script khởi động
├── run_tests.py          # Script chạy test tự động
└── README.md             # Documentation
```

## Cài đặt

1. Cài đặt Python 3.8+ (khuyến nghị Python 3.9)
2. Cài đặt dependencies:

```bash
pip install -r requirements.txt
```

## Chạy service

### Khởi động server

```bash
python startup.py
```

Server sẽ chạy tại http://127.0.0.1:5000

### Chạy test API

```bash
python tests/test_api.py
```

### Tự động khởi động server và chạy test

```bash
python run_tests.py
```

## API Endpoints

### 1. Health Check

```
GET /api/health
```

Kiểm tra trạng thái hoạt động của service.

### 2. Check Duplicate

```
POST /api/check-duplicate
Content-Type: application/json

{
  "new_question": "What is the capital of Vietnam?",
  "existing_questions": [
    {"id": 1, "content": "Which city is Vietnam's capital?"},
    {"id": 2, "content": "What is machine learning?"}
  ]
}
```

Kiểm tra độ tương đồng của một câu hỏi mới với danh sách câu hỏi hiện có.

### 3. Batch Check

```
POST /api/batch-check
Content-Type: application/json

{
  "questions": [
    {"id": 1, "content": "What is AI?"},
    {"id": 2, "content": "What is artificial intelligence?"}
  ]
}
```

Kiểm tra độ tương đồng giữa tất cả các câu hỏi trong một tập hợp.

## Tích hợp với Spring Boot

Trong Java Spring Boot, bạn có thể sử dụng RestTemplate để gọi API của service này:

```java
@Service
public class DuplicateDetectionService {
    
    private final RestTemplate restTemplate;
    private final String aiServiceUrl = "http://localhost:5000/api";
    
    public DuplicateDetectionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public Map<String, Object> checkDuplicate(String newQuestion, List<QuestionDto> existingQuestions) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("new_question", newQuestion);
        requestBody.put("existing_questions", existingQuestions);
        
        ResponseEntity<Map> response = restTemplate.postForEntity(
            aiServiceUrl + "/check-duplicate", 
            requestBody, 
            Map.class
        );
        
        return response.getBody();
    }
}
```

## Cấu hình

Cấu hình được lưu trong file `.env` và `src/config.py`:

- `SIMILARITY_THRESHOLD`: Ngưỡng xác định độ tương đồng (mặc định: 0.75)
- `MODEL_NAME`: Mô hình sentence transformer (mặc định: all-MiniLM-L6-v2)
- `MAX_QUESTIONS_PER_REQUEST`: Số lượng câu hỏi tối đa mỗi request (mặc định: 100)
