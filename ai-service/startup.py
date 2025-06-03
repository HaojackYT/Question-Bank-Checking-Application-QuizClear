"""
Startup script for AI Duplicate Detection Service
"""
import os
import sys
import subprocess
import time

def create_model_dir():
    """Create models directory if it doesn't exist"""
    models_dir = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'models')
    if not os.path.exists(models_dir):
        print(f"Creating models directory: {models_dir}")
        os.makedirs(models_dir)
    else:
        print(f"Models directory already exists: {models_dir}")

def start_flask_app():
    """Start the Flask application"""
    print("Starting AI Duplicate Detection Service...")
    
    # Set environment variables
    os.environ['FLASK_ENV'] = 'development'
    os.environ['FLASK_HOST'] = '127.0.0.1'
    os.environ['FLASK_PORT'] = '5000'
    os.environ['FLASK_DEBUG'] = 'True'
    
    # Add current directory to Python path
    sys.path.append(os.path.join(os.path.dirname(os.path.abspath(__file__)), 'src'))
    
    try:
        # Run the Flask app
        from src.app import app
        app.run(
            host='127.0.0.1',
            port=5000,
            debug=True
        )
    except ImportError as e:
        print(f"Error importing Flask app: {e}")
        print("\nPlease make sure all dependencies are installed:")
        print("pip install -r requirements.txt")
        sys.exit(1)
    except Exception as e:
        print(f"Error starting Flask app: {e}")
        sys.exit(1)

if __name__ == "__main__":
    # Ensure models directory exists
    create_model_dir()
    
    # Start Flask app
    start_flask_app()
