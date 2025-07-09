"""
AI Duplicate Detection Module using Sentence Transformers
Module phát hiện câu hỏi trùng lặp sử dụng AI Sentence Transformers
"""
import numpy as np
import logging
import os
import sys
from sentence_transformers import SentenceTransformer, util
from sklearn.metrics.pairwise import cosine_similarity
from typing import List, Dict, Tuple

# Thêm thư mục cha vào đường dẫn để import được các module
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
from src.config import Config

# Thiết lập logging để ghi nhật ký
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class DuplicateDetector:
    def __init__(self):
        """Khởi tạo detector phát hiện trùng lặp với model sentence transformer"""
        self.model = None  # Model AI chưa được load
        self.model_name = Config.MODEL_NAME  # Tên model từ config
        self.cache_dir = Config.get_model_path()  # Thư mục lưu model
        self.threshold = Config.SIMILARITY_THRESHOLD  # Ngưỡng độ tương tự
        self._load_model()  # Load model ngay khi khởi tạo
    
    def _load_model(self):
        """Load model sentence transformer từ cache hoặc download"""
        try:
            logger.info(f"Đang load model: {self.model_name}")
            logger.info(f"Thư mục cache: {self.cache_dir}")
            
            # Load model với thư mục cache tùy chỉnh
            self.model = SentenceTransformer(
                self.model_name, 
                cache_folder=self.cache_dir
            )
            
            logger.info("Model đã được load thành công!")
            
        except Exception as e:
            logger.error(f"Lỗi khi load model: {str(e)}")
            raise e
    def encode_questions(self, questions: List[str]) -> np.ndarray:
        """Chuyển đổi câu hỏi thành vector số (embeddings)"""
        try:
            if not questions:
                return np.array([])
            
            # Làm sạch và chuẩn bị câu hỏi
            cleaned_questions = [q.strip() for q in questions if q.strip()]
            
            if not cleaned_questions:
                return np.array([])
            
            # Tạo embeddings (vector đại diện cho ý nghĩa câu hỏi)
            embeddings = self.model.encode(cleaned_questions)
            logger.info(f"Đã tạo embeddings cho {len(cleaned_questions)} câu hỏi")
            
            return embeddings
            
        except Exception as e:
            logger.error(f"Lỗi khi encode câu hỏi: {str(e)}")
            raise e
    def calculate_similarity(self, embedding1: np.ndarray, embedding2: np.ndarray) -> float:
        """Tính độ tương tự giữa hai vector embedding (từ 0 đến 1)"""
        try:
            # Reshape nếu cần thiết (chuyển thành ma trận 2D)
            if embedding1.ndim == 1:
                embedding1 = embedding1.reshape(1, -1)
            if embedding2.ndim == 1:
                embedding2 = embedding2.reshape(1, -1)
            
            # Tính độ tương tự cosine (0 = hoàn toàn khác, 1 = giống hệt)
            similarity = cosine_similarity(embedding1, embedding2)[0][0]
            return float(similarity)
            
        except Exception as e:
            logger.error(f"Lỗi khi tính độ tương tự: {str(e)}")
            return 0.0
    def find_duplicates(self, new_question: str, existing_questions: List[Dict]) -> List[Dict]:
        """
        Tìm các câu hỏi trùng lặp cho một câu hỏi mới
        
        Args:
            new_question (str): Câu hỏi mới cần kiểm tra
            existing_questions (List[Dict]): Danh sách câu hỏi đã có với format:
                [{"id": int, "content": str}, ...]
        
        Returns:
            List[Dict]: Danh sách câu hỏi tương tự với điểm số
        """
        try:
            if not new_question.strip():
                return []
            
            if not existing_questions:
                return []
            
            # Lấy nội dung các câu hỏi đã có
            existing_contents = [q.get('content', '') for q in existing_questions]
            existing_contents = [c for c in existing_contents if c.strip()]
            
            if not existing_contents:
                return []
            
            # Encode câu hỏi mới thành vector
            new_embedding = self.encode_questions([new_question])
            if new_embedding.size == 0:
                return []
            
            # Encode các câu hỏi đã có thành vector
            existing_embeddings = self.encode_questions(existing_contents)
            if existing_embeddings.size == 0:
                return []
            
            # Tính độ tương tự với từng câu hỏi
            similarities = []
            for i, existing_embedding in enumerate(existing_embeddings):
                similarity = self.calculate_similarity(new_embedding[0], existing_embedding)
                
                # Chỉ lấy những câu có độ tương tự trên ngưỡng
                if similarity >= self.threshold:
                    similarities.append({
                        'question_id': existing_questions[i].get('id'),
                        'content': existing_questions[i].get('content'),
                        'similarity_score': round(similarity, 4)
                    })
            
            # Sắp xếp theo độ tương tự (từ cao xuống thấp)
            similarities.sort(key=lambda x: x['similarity_score'], reverse=True)
            
            logger.info(f"Tìm thấy {len(similarities)} câu hỏi tương tự trên ngưỡng {self.threshold}")
            
            return similarities
            
        except Exception as e:
            logger.error(f"Lỗi khi tìm câu hỏi trùng lặp: {str(e)}")
            return []
    def batch_check_duplicates(self, questions: List[Dict]) -> Dict:
        """
        Kiểm tra trùng lặp cho nhiều câu hỏi cùng một lúc
        
        Args:
            questions (List[Dict]): Danh sách câu hỏi cần kiểm tra
        
        Returns:
            Dict: Kết quả cho từng câu hỏi
        """
        try:
            results = {}
            
            for i, question in enumerate(questions):
                question_id = question.get('id')
                content = question.get('content', '')
                
                # Kiểm tra với các câu hỏi khác trong batch
                other_questions = questions[:i] + questions[i+1:]
                
                duplicates = self.find_duplicates(content, other_questions)
                
                results[question_id] = {
                    'question_content': content,
                    'duplicates_found': len(duplicates),
                    'similar_questions': duplicates
                }
            
            return results
            
        except Exception as e:
            logger.error(f"Lỗi trong batch check: {str(e)}")
            return {}
    def health_check(self) -> Dict:
        """Kiểm tra xem service có hoạt động tốt không"""
        try:
            # Test với 2 câu hỏi đơn giản
            test_questions = ["What is AI?", "What is artificial intelligence?"]
            embeddings = self.encode_questions(test_questions)
            
            if embeddings.size > 0:
                similarity = self.calculate_similarity(embeddings[0], embeddings[1])
                
                return {
                    'status': 'healthy',
                    'model_loaded': True,
                    'model_name': self.model_name,
                    'test_similarity': round(similarity, 4),
                    'threshold': self.threshold
                }
            else:
                return {
                    'status': 'unhealthy',
                    'model_loaded': False,
                    'error': 'Không thể tạo embeddings'
                }
                
        except Exception as e:
            return {
                'status': 'unhealthy',
                'model_loaded': False,
                'error': str(e)
            }
