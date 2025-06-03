"""
Flask API Server for AI Duplicate Detection Service
"""
from flask import Flask, request, jsonify
from flask_cors import CORS
import logging
import os
import sys
from datetime import datetime

# Add current directory to path
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from config import config
from duplicate_detector import DuplicateDetector

# Setup logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# Initialize Flask app
app = Flask(__name__)

# Get configuration
config_name = os.getenv('FLASK_ENV', 'default')
app_config = config[config_name]

# Setup CORS
CORS(app, origins=app_config.CORS_ORIGINS)

# Initialize AI detector (will be loaded on first request)
detector = None

def get_detector():
    """Get or initialize the detector"""
    global detector
    if detector is None:
        logger.info("Initializing AI Duplicate Detector...")
        detector = DuplicateDetector()
        logger.info("Detector initialized successfully!")
    return detector

@app.route('/api/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    try:
        detector = get_detector()
        health_status = detector.health_check()
        
        return jsonify({
            'timestamp': datetime.now().isoformat(),
            'service': 'AI Duplicate Detection Service',
            'version': '1.0.0',
            'ai_status': health_status
        }), 200
        
    except Exception as e:
        logger.error(f"Health check failed: {str(e)}")
        return jsonify({
            'timestamp': datetime.now().isoformat(),
            'service': 'AI Duplicate Detection Service',
            'version': '1.0.0',
            'ai_status': {
                'status': 'unhealthy',
                'error': str(e)
            }
        }), 500

@app.route('/api/check-duplicate', methods=['POST'])
def check_duplicate():
    """
    Check for duplicate questions
    
    Request body:
    {
        "new_question": "What is the capital of Vietnam?",
        "existing_questions": [
            {"id": 1, "content": "Which city is Vietnam's capital?"},
            {"id": 2, "content": "What is machine learning?"}
        ]
    }
    """
    try:
        # Validate request
        if not request.is_json:
            return jsonify({
                'error': 'Content-Type must be application/json'
            }), 400
        
        data = request.get_json()
        
        # Validate required fields
        if 'new_question' not in data:
            return jsonify({
                'error': 'Missing required field: new_question'
            }), 400
        
        if 'existing_questions' not in data:
            return jsonify({
                'error': 'Missing required field: existing_questions'
            }), 400
        
        new_question = data['new_question']
        existing_questions = data['existing_questions']
        
        # Validate input types
        if not isinstance(new_question, str):
            return jsonify({
                'error': 'new_question must be a string'
            }), 400
        
        if not isinstance(existing_questions, list):
            return jsonify({
                'error': 'existing_questions must be a list'
            }), 400
        
        # Validate question format
        for i, q in enumerate(existing_questions):
            if not isinstance(q, dict):
                return jsonify({
                    'error': f'existing_questions[{i}] must be an object'
                }), 400
            
            if 'id' not in q or 'content' not in q:
                return jsonify({
                    'error': f'existing_questions[{i}] must have id and content fields'
                }), 400
        
        # Check limits
        if len(existing_questions) > app_config.MAX_QUESTIONS_PER_REQUEST:
            return jsonify({
                'error': f'Too many questions. Maximum allowed: {app_config.MAX_QUESTIONS_PER_REQUEST}'
            }), 400
        
        # Get detector and find duplicates
        detector = get_detector()
        similar_questions = detector.find_duplicates(new_question, existing_questions)
        
        # Prepare response
        response = {
            'timestamp': datetime.now().isoformat(),
            'new_question': new_question,
            'total_checked': len(existing_questions),
            'duplicates_found': len(similar_questions),
            'threshold_used': detector.threshold,
            'similar_questions': similar_questions
        }
        
        logger.info(f"Processed duplicate check: {len(similar_questions)} duplicates found")
        
        return jsonify(response), 200
        
    except Exception as e:
        logger.error(f"Error in check_duplicate: {str(e)}")
        return jsonify({
            'error': 'Internal server error',
            'message': str(e)
        }), 500

@app.route('/api/batch-check', methods=['POST'])
def batch_check():
    """
    Check duplicates for multiple questions at once
    
    Request body:
    {
        "questions": [
            {"id": 1, "content": "What is AI?"},
            {"id": 2, "content": "What is artificial intelligence?"}
        ]
    }
    """
    try:
        # Validate request
        if not request.is_json:
            return jsonify({
                'error': 'Content-Type must be application/json'
            }), 400
        
        data = request.get_json()
        
        if 'questions' not in data:
            return jsonify({
                'error': 'Missing required field: questions'
            }), 400
        
        questions = data['questions']
        
        if not isinstance(questions, list):
            return jsonify({
                'error': 'questions must be a list'
            }), 400
        
        if len(questions) > app_config.MAX_QUESTIONS_PER_REQUEST:
            return jsonify({
                'error': f'Too many questions. Maximum allowed: {app_config.MAX_QUESTIONS_PER_REQUEST}'
            }), 400
        
        # Get detector and process batch
        detector = get_detector()
        results = detector.batch_check_duplicates(questions)
        
        response = {
            'timestamp': datetime.now().isoformat(),
            'total_questions': len(questions),
            'threshold_used': detector.threshold,
            'results': results
        }
        
        logger.info(f"Processed batch check for {len(questions)} questions")
        
        return jsonify(response), 200
        
    except Exception as e:
        logger.error(f"Error in batch_check: {str(e)}")
        return jsonify({
            'error': 'Internal server error',
            'message': str(e)
        }), 500

@app.errorhandler(404)
def not_found(error):
    """Handle 404 errors"""
    return jsonify({
        'error': 'Endpoint not found',
        'available_endpoints': [
            'GET /api/health',
            'POST /api/check-duplicate',
            'POST /api/batch-check'
        ]
    }), 404

@app.errorhandler(500)
def internal_error(error):
    """Handle 500 errors"""
    return jsonify({
        'error': 'Internal server error'
    }), 500

if __name__ == '__main__':
    logger.info("Starting AI Duplicate Detection Service...")
    logger.info(f"Configuration: {config_name}")
    logger.info(f"Host: {app_config.FLASK_HOST}")
    logger.info(f"Port: {app_config.FLASK_PORT}")
    logger.info(f"Debug: {app_config.FLASK_DEBUG}")
    
    app.run(
        host=app_config.FLASK_HOST,
        port=app_config.FLASK_PORT,
        debug=app_config.FLASK_DEBUG
    )
