"""
AI Duplicate Detection Module using Sentence Transformers
"""
import numpy as np
import logging
import os
import sys
from sentence_transformers import SentenceTransformer, util
from sklearn.metrics.pairwise import cosine_similarity
from typing import List, Dict, Tuple

# Add parent directory to path for imports
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
from src.config import Config

# Setup logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class DuplicateDetector:
    def __init__(self):
        """Initialize the duplicate detector with sentence transformer model"""
        self.model = None
        self.model_name = Config.MODEL_NAME
        self.cache_dir = Config.get_model_path()
        self.threshold = Config.SIMILARITY_THRESHOLD
        self._load_model()
    
    def _load_model(self):
        """Load the sentence transformer model"""
        try:
            logger.info(f"Loading model: {self.model_name}")
            logger.info(f"Cache directory: {self.cache_dir}")
            
            # Load model with custom cache directory
            self.model = SentenceTransformer(
                self.model_name, 
                cache_folder=self.cache_dir
            )
            
            logger.info("Model loaded successfully!")
            
        except Exception as e:
            logger.error(f"Error loading model: {str(e)}")
            raise e
    
    def encode_questions(self, questions: List[str]) -> np.ndarray:
        """Convert questions to embeddings"""
        try:
            if not questions:
                return np.array([])
            
            # Clean and prepare questions
            cleaned_questions = [q.strip() for q in questions if q.strip()]
            
            if not cleaned_questions:
                return np.array([])
            
            # Generate embeddings
            embeddings = self.model.encode(cleaned_questions)
            logger.info(f"Generated embeddings for {len(cleaned_questions)} questions")
            
            return embeddings
            
        except Exception as e:
            logger.error(f"Error encoding questions: {str(e)}")
            raise e
    
    def calculate_similarity(self, embedding1: np.ndarray, embedding2: np.ndarray) -> float:
        """Calculate cosine similarity between two embeddings"""
        try:
            # Reshape if needed
            if embedding1.ndim == 1:
                embedding1 = embedding1.reshape(1, -1)
            if embedding2.ndim == 1:
                embedding2 = embedding2.reshape(1, -1)
            
            # Calculate cosine similarity
            similarity = cosine_similarity(embedding1, embedding2)[0][0]
            return float(similarity)
            
        except Exception as e:
            logger.error(f"Error calculating similarity: {str(e)}")
            return 0.0
    
    def find_duplicates(self, new_question: str, existing_questions: List[Dict]) -> List[Dict]:
        """
        Find duplicate questions for a new question
        
        Args:
            new_question (str): The new question to check
            existing_questions (List[Dict]): List of existing questions with format:
                [{"id": int, "content": str}, ...]
        
        Returns:
            List[Dict]: List of similar questions with similarity scores
        """
        try:
            if not new_question.strip():
                return []
            
            if not existing_questions:
                return []
            
            # Extract question contents
            existing_contents = [q.get('content', '') for q in existing_questions]
            existing_contents = [c for c in existing_contents if c.strip()]
            
            if not existing_contents:
                return []
            
            # Encode new question
            new_embedding = self.encode_questions([new_question])
            if new_embedding.size == 0:
                return []
            
            # Encode existing questions
            existing_embeddings = self.encode_questions(existing_contents)
            if existing_embeddings.size == 0:
                return []
            
            # Calculate similarities
            similarities = []
            for i, existing_embedding in enumerate(existing_embeddings):
                similarity = self.calculate_similarity(new_embedding[0], existing_embedding)
                
                # Only include if above threshold
                if similarity >= self.threshold:
                    similarities.append({
                        'question_id': existing_questions[i].get('id'),
                        'content': existing_questions[i].get('content'),
                        'similarity_score': round(similarity, 4)
                    })
            
            # Sort by similarity score (descending)
            similarities.sort(key=lambda x: x['similarity_score'], reverse=True)
            
            logger.info(f"Found {len(similarities)} similar questions above threshold {self.threshold}")
            
            return similarities
            
        except Exception as e:
            logger.error(f"Error finding duplicates: {str(e)}")
            return []
    
    def batch_check_duplicates(self, questions: List[Dict]) -> Dict:
        """
        Check duplicates for multiple questions at once
        
        Args:
            questions (List[Dict]): List of questions to check
        
        Returns:
            Dict: Results for each question
        """
        try:
            results = {}
            
            for i, question in enumerate(questions):
                question_id = question.get('id')
                content = question.get('content', '')
                
                # Check against other questions in the batch
                other_questions = questions[:i] + questions[i+1:]
                
                duplicates = self.find_duplicates(content, other_questions)
                
                results[question_id] = {
                    'question_content': content,
                    'duplicates_found': len(duplicates),
                    'similar_questions': duplicates
                }
            
            return results
            
        except Exception as e:
            logger.error(f"Error in batch check: {str(e)}")
            return {}
    
    def health_check(self) -> Dict:
        """Check if the service is healthy"""
        try:
            # Test with simple questions
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
                    'error': 'Failed to generate embeddings'
                }
                
        except Exception as e:
            return {
                'status': 'unhealthy',
                'model_loaded': False,
                'error': str(e)
            }
