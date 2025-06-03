"""
Configuration file for AI Duplicate Detection Service
"""
import os
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

class Config:
    # Flask settings
    FLASK_HOST = os.getenv('FLASK_HOST', '127.0.0.1')
    FLASK_PORT = int(os.getenv('FLASK_PORT', 5000))
    FLASK_DEBUG = os.getenv('FLASK_DEBUG', 'True').lower() == 'true'
    
    # AI Model settings
    MODEL_NAME = os.getenv('MODEL_NAME', 'all-MiniLM-L6-v2')
    MODEL_CACHE_DIR = os.getenv('MODEL_CACHE_DIR', '../models')
    
    # Similarity threshold
    SIMILARITY_THRESHOLD = float(os.getenv('SIMILARITY_THRESHOLD', 0.75))
    
    # API settings
    MAX_QUESTIONS_PER_REQUEST = int(os.getenv('MAX_QUESTIONS_PER_REQUEST', 100))
    
    # CORS settings
    CORS_ORIGINS = os.getenv('CORS_ORIGINS', 'http://localhost:8080').split(',')
    
    @classmethod
    def get_model_path(cls):
        """Get absolute path to model cache directory"""
        current_dir = os.path.dirname(os.path.abspath(__file__))
        return os.path.join(current_dir, cls.MODEL_CACHE_DIR)

# Development configuration
class DevelopmentConfig(Config):
    FLASK_DEBUG = True
    
# Production configuration  
class ProductionConfig(Config):
    FLASK_DEBUG = False
    FLASK_HOST = '0.0.0.0'

# Configuration mapping
config = {
    'development': DevelopmentConfig,
    'production': ProductionConfig,
    'default': DevelopmentConfig
}
