"""
Helper script to run the test after ensuring the server is running
"""
import os
import sys
import time
import subprocess
import requests

def is_server_running():
    """Check if the server is running"""
    try:
        response = requests.get("http://127.0.0.1:5000/api/health")
        return response.status_code == 200
    except:
        return False

def start_server():
    """Start the Flask server"""
    print("Starting AI Service...")
    
    # Start the server in a new process
    server_process = subprocess.Popen(
        ["python", "startup.py"],
        cwd=os.path.dirname(os.path.abspath(__file__))
    )
    
    # Wait for server to start
    print("Waiting for server to start...")
    max_attempts = 20
    for i in range(max_attempts):
        if is_server_running():
            print("Server is running!")
            return server_process
        print(f"Attempt {i+1}/{max_attempts}...")
        time.sleep(3)
    
    print("Failed to start server after multiple attempts")
    server_process.terminate()
    sys.exit(1)

def run_tests():
    """Run the test script"""
    print("\nRunning tests...")
    subprocess.call(
        ["python", "tests/test_api.py"],
        cwd=os.path.dirname(os.path.abspath(__file__))
    )

if __name__ == "__main__":
    # Start server
    server_process = start_server()
    
    try:
        # Run tests
        run_tests()
    finally:
        # Terminate server process
        print("\nShutting down server...")
        server_process.terminate()
        server_process.wait()
