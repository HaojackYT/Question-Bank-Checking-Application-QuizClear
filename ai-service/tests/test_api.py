"""
Simple test script for AI Duplicate Detection API
"""
import requests
import json
import time

# API base URL
BASE_URL = "http://127.0.0.1:5000/api"

def test_health():
    """Test health endpoint"""
    print("🔍 Testing health endpoint...")
    try:
        response = requests.get(f"{BASE_URL}/health")
        print(f"Status: {response.status_code}")
        print(f"Response: {json.dumps(response.json(), indent=2)}")
        return response.status_code == 200
    except Exception as e:
        print(f"❌ Health check failed: {e}")
        return False

def test_duplicate_check():
    """Test duplicate check endpoint"""
    print("\n🔍 Testing duplicate check...")
    
    test_data = {
        "new_question": "What is the capital of Vietnam?",
        "existing_questions": [
            {"id": 1, "content": "Which city is Vietnam's capital?"},
            {"id": 2, "content": "What is the capital city of Vietnam?"},
            {"id": 3, "content": "What is machine learning?"},
            {"id": 4, "content": "Where is Hanoi located?"},
            {"id": 5, "content": "Vietnam's capital is which city?"}
        ]
    }
    
    try:
        response = requests.post(
            f"{BASE_URL}/check-duplicate",
            json=test_data,
            headers={'Content-Type': 'application/json'}
        )
        
        print(f"Status: {response.status_code}")
        print(f"Response: {json.dumps(response.json(), indent=2)}")
        return response.status_code == 200
        
    except Exception as e:
        print(f"❌ Duplicate check failed: {e}")
        return False

def test_batch_check():
    """Test batch check endpoint"""
    print("\n🔍 Testing batch check...")
    
    test_data = {
        "questions": [
            {"id": 1, "content": "What is artificial intelligence?"},
            {"id": 2, "content": "What is AI?"},
            {"id": 3, "content": "Define machine learning"},
            {"id": 4, "content": "What is ML?"}
        ]
    }
    
    try:
        response = requests.post(
            f"{BASE_URL}/batch-check",
            json=test_data,
            headers={'Content-Type': 'application/json'}
        )
        
        print(f"Status: {response.status_code}")
        print(f"Response: {json.dumps(response.json(), indent=2)}")
        return response.status_code == 200
        
    except Exception as e:
        print(f"❌ Batch check failed: {e}")
        return False

def main():
    """Run all tests"""
    print("🚀 Starting AI Service API Tests\n")
    
    # Wait for service to start
    print("⏳ Waiting for service to start...")
    time.sleep(5)  # Tăng thời gian chờ lên 5 giây
    
    # Run tests
    tests = [
        ("Health Check", test_health),
        ("Duplicate Check", test_duplicate_check),
        ("Batch Check", test_batch_check)
    ]
    
    results = []
    for test_name, test_func in tests:
        print(f"\n{'='*50}")
        print(f"Running: {test_name}")
        print('='*50)
        
        success = test_func()
        results.append((test_name, success))
        
        if success:
            print(f"✅ {test_name} PASSED")
        else:
            print(f"❌ {test_name} FAILED")
    
    # Summary
    print(f"\n{'='*50}")
    print("TEST SUMMARY")
    print('='*50)
    
    passed = sum(1 for _, success in results if success)
    total = len(results)
    
    for test_name, success in results:
        status = "✅ PASS" if success else "❌ FAIL"
        print(f"{test_name}: {status}")
    
    print(f"\nTotal: {passed}/{total} tests passed")
    
    if passed == total:
        print("🎉 All tests passed! AI Service is working correctly!")
    else:
        print("⚠️ Some tests failed. Check the service logs.")

if __name__ == "__main__":
    main()
