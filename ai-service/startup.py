"""
Startup script for AI Duplicate Detection Service
"""
import os
import sys
import subprocess
import time
import shutil

def setup_models():
    """Setup models directory and copy from backup if needed"""
    models_dir = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'models')
    models_backup_dir = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'models_backup')
    
    # Create models directory if it doesn't exist
    if not os.path.exists(models_dir):
        print(f"Creating models directory: {models_dir}")
        os.makedirs(models_dir)
    
    # Check if models are missing and copy from backup
    sentence_transformer_dir = os.path.join(models_dir, 'sentence-transformers_all-MiniLM-L6-v2')
    if not os.path.exists(sentence_transformer_dir) and os.path.exists(models_backup_dir):
        print("Models not found in persistent volume. Copying from backup...")
        try:
            if os.path.exists(models_backup_dir):
                shutil.copytree(models_backup_dir, models_dir, dirs_exist_ok=True)
                print("Models copied successfully from backup!")
            else:
                print("Warning: No backup models found!")
        except Exception as e:
            print(f"Error copying models: {e}")
    else:
        print("Models already exist in persistent volume. Skipping copy.")

def start_flask_app():
    """Start the Flask application"""
    print("Starting AI Duplicate Detection Service...")
    
    # Set environment variables for Docker
    os.environ['FLASK_ENV'] = 'development'
    os.environ['FLASK_HOST'] = '0.0.0.0'  # Changed to accept connections from any IP
    os.environ['FLASK_PORT'] = '5000'
    os.environ['FLASK_DEBUG'] = 'True'
    
    # Add current directory to Python path
    sys.path.append(os.path.join(os.path.dirname(os.path.abspath(__file__)), 'src'))
    
    try:
        # Run the Flask app
        from src.app import app
        app.run(
            host='0.0.0.0',  # Changed to accept connections from any IP
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
    # Setup models directory and copy from backup if needed
    setup_models()
    
    # Start Flask app
    start_flask_app()
