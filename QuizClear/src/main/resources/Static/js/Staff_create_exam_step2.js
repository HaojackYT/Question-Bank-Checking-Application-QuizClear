// Lấy input tổng số câu hỏi
const totalQuestionsInput = document.getElementById("total-questions");

// Cấu hình các slider và cột tương ứng
const sliders = [
    {
        range: document.getElementById("range-basic"),
        valueEl: document.getElementById("range-value-basic"),
        percentEl: document.querySelector("#range-basic").parentElement.querySelector("p span"),
        columEl: document.getElementById("Basic"),
        valuePercentEl: document.getElementById("value-basic")
    },
    {
        range: document.getElementById("range-Intermediate"),
        valueEl: document.getElementById("range-value-Intermediate"),
        percentEl: document.querySelector("#range-Intermediate").parentElement.querySelector("p span"),
        columEl: document.getElementById("Intermediate"),
        valuePercentEl: document.getElementById("value-intermediate")
    },
    {
        range: document.getElementById("range-Advanced"),
        valueEl: document.getElementById("range-value-Advanced"),
        percentEl: document.querySelector("#range-Advanced").parentElement.querySelector("p span"),
        columEl: document.getElementById("Advanced"),
        valuePercentEl: document.getElementById("value-advanced")
    },
    {
        range: document.getElementById("range-Expert"),
        valueEl: document.getElementById("range-value-Expert"),
        percentEl: document.querySelector("#range-Expert").parentElement.querySelector("p span"),
        columEl: document.getElementById("Expert"),
        valuePercentEl: document.getElementById("value-expert")
    }
];

// Lấy thẻ hiển thị tổng phần trăm
const totalPercentageDisplay = document.querySelector("p[style*='color: #2563EB'] span");

// Hàm tính tổng số câu hỏi từ các thanh kéo
function getTotalSliderValue() {
    return sliders.reduce((sum, s) => sum + parseInt(s.range.value), 0);
}

// Hàm cập nhật phần trăm, số câu hỏi và độ cao cột
function updateSliderValues(sliderObj, totalQuestions) {
    const value = parseInt(sliderObj.range.value);
    sliderObj.valueEl.textContent = value; // Hiển thị số câu hỏi
    const percentage = totalQuestions > 0 ? ((value / totalQuestions) * 100).toFixed(1) : 0;
    sliderObj.percentEl.textContent = percentage; // Hiển thị phần trăm trên thanh kéo
    sliderObj.valuePercentEl.textContent = percentage; // Hiển thị phần trăm dưới cột
    sliderObj.columEl.style.height = `${percentage}px`; // Cập nhật độ cao cột (1% = 1px)
}

// Hàm cập nhật tổng phần trăm
function updateTotalPercentage() {
    const totalQuestions = parseInt(totalQuestionsInput.value) || 0;
    const totalSliderValue = getTotalSliderValue();
    const totalPercentage = totalQuestions > 0 ? ((totalSliderValue / totalQuestions) * 100).toFixed(1) : 0;
    totalPercentageDisplay.textContent = totalPercentage; // Cập nhật tổng phần trăm
}

// Hàm cập nhật màu nền thanh kéo
function updateSliderBackground(slider) {
    const min = slider.min ? parseFloat(slider.min) : 0;
    const max = slider.max ? parseFloat(slider.max) : 100;
    const val = ((slider.value - min) / (max - min)) * 100;
    slider.style.background = `linear-gradient(to right, #3B82F6 ${val}%, #DBEAFE ${val}%)`;
}

// Xử lý sự kiện cho từng thanh kéo
sliders.forEach((sliderObj) => {
    sliderObj.range.addEventListener("input", () => {
        const totalQuestions = parseInt(totalQuestionsInput.value) || 0;
        const currentTotal = getTotalSliderValue();
        const previousValue = parseInt(sliderObj.valueEl.textContent);
        const currentValue = parseInt(sliderObj.range.value);

        // Nếu tổng bằng hoặc vượt quá totalQuestions và đang tăng, giữ nguyên giá trị cũ
        if (currentValue > previousValue && currentTotal > totalQuestions) {
            sliderObj.range.value = previousValue;
            return;
        }

        // Cập nhật giá trị hiển thị, phần trăm và độ cao cột
        updateSliderValues(sliderObj, totalQuestions);
        updateTotalPercentage();
        updateSliderBackground(sliderObj.range);
    });

    // Cập nhật màu nền ban đầu
    updateSliderBackground(sliderObj.range);
});

// Xử lý khi nhập total-questions
totalQuestionsInput.addEventListener("input", () => {
    const total = parseInt(totalQuestionsInput.value);
    if (isNaN(total) || total < 0) {
        totalQuestionsInput.value = 0;
    }
    const totalQuestions = parseInt(totalQuestionsInput.value) || 0;
    const currentTotal = getTotalSliderValue();

    // Nếu tổng câu hỏi hiện tại vượt quá totalQuestions, giảm giá trị các thanh kéo
    if (currentTotal > totalQuestions) {
        let excess = currentTotal - totalQuestions;
        for (let i = sliders.length - 1; i >= 0 && excess > 0; i--) {
            const currentValue = parseInt(sliders[i].range.value);
            const reduction = Math.min(currentValue, excess);
            sliders[i].range.value = currentValue - reduction;
            excess -= reduction;
            updateSliderValues(sliders[i], totalQuestions);
            updateSliderBackground(sliders[i].range);
        }
    }

    // Cập nhật lại tất cả giá trị hiển thị và độ cao cột
    sliders.forEach(sliderObj => updateSliderValues(sliderObj, totalQuestions));
    updateTotalPercentage();
});

// Subject scope filtering for exam creation
let currentUserScope = {
    userId: null,
    userRole: null,
    accessibleDepartmentIds: [],
    accessibleSubjectIds: [],
    selectedSubjectId: null,
    availableQuestions: []
};

// Available questions data filtered by subject scope
let questionsData = [];

// Initialize subject scope for exam creation
async function initializeSubjectScope() {
    try {
        // Get user scope from session or API
        const response = await fetch('/api/user/current-scope', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        if (response.ok) {
            currentUserScope = await response.json();
            console.log('Subject scope initialized for exam creation:', currentUserScope);
            
            // Get selected subject from previous step or URL params
            const urlParams = new URLSearchParams(window.location.search);
            currentUserScope.selectedSubjectId = urlParams.get('subjectId') || getSelectedSubjectFromStep1();
            
            if (currentUserScope.selectedSubjectId) {
                await loadQuestionsForSubject(currentUserScope.selectedSubjectId);
            }
        } else {
            console.error('Failed to load user scope for exam creation');
        }
    } catch (error) {
        console.error('Error initializing subject scope:', error);
    }
}

// Load questions filtered by subject scope
async function loadQuestionsForSubject(subjectId) {
    try {
        const params = new URLSearchParams({
            subjectId: subjectId,
            requestingUserId: currentUserScope.userId,
            userRole: currentUserScope.userRole,
            status: 'APPROVED' // Only load approved questions for exam creation
        });

        const response = await fetch(`/api/questions/by-subject?${params}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            questionsData = await response.json();
            console.log(`Loaded ${questionsData.length} questions for subject ${subjectId}`);
            
            // Update UI with available questions count by difficulty
            updateAvailableQuestionCounts();
            
            // Validate if enough questions are available
            validateQuestionAvailability();
        } else {
            console.error('Failed to load questions for subject');
            questionsData = [];
        }
    } catch (error) {
        console.error('Error loading questions for subject:', error);
        questionsData = [];
    }
}

// Update available question counts by difficulty level
function updateAvailableQuestionCounts() {
    const difficultyCounts = {
        'BASIC': 0,
        'INTERMEDIATE': 0,
        'ADVANCED': 0,
        'EXPERT': 0
    };

    questionsData.forEach(question => {
        if (question.difficultyLevel && difficultyCounts.hasOwnProperty(question.difficultyLevel)) {
            difficultyCounts[question.difficultyLevel]++;
        }
    });

    // Update UI to show available counts
    updateDifficultyLevelUI(difficultyCounts);
}

// Update difficulty level UI with available question counts
function updateDifficultyLevelUI(counts) {
    const difficultyMappings = [
        { level: 'BASIC', elementId: 'Basic', maxElement: 'max-basic' },
        { level: 'INTERMEDIATE', elementId: 'Intermediate', maxElement: 'max-intermediate' },
        { level: 'ADVANCED', elementId: 'Advanced', maxElement: 'max-advanced' },
        { level: 'EXPERT', elementId: 'Expert', maxElement: 'max-expert' }
    ];

    difficultyMappings.forEach(mapping => {
        const availableCount = counts[mapping.level] || 0;
        
        // Update max attribute for sliders
        const slider = document.getElementById(`range-${mapping.level.toLowerCase()}`);
        if (slider) {
            slider.max = availableCount;
            slider.setAttribute('data-available', availableCount);
        }
        
        // Update max display element
        const maxElement = document.getElementById(mapping.maxElement);
        if (maxElement) {
            maxElement.textContent = availableCount;
        }
        
        // Add availability indicator
        const availabilityIndicator = document.querySelector(`#${mapping.elementId} .availability-indicator`);
        if (availabilityIndicator) {
            availabilityIndicator.textContent = `Available: ${availableCount}`;
            availabilityIndicator.className = `availability-indicator ${availableCount > 0 ? 'available' : 'unavailable'}`;
        } else {
            // Create availability indicator if it doesn't exist
            const indicator = document.createElement('div');
            indicator.className = `availability-indicator ${availableCount > 0 ? 'available' : 'unavailable'}`;
            indicator.textContent = `Available: ${availableCount}`;
            const column = document.getElementById(mapping.elementId);
            if (column) {
                column.appendChild(indicator);
            }
        }
    });
}

// Validate if enough questions are available for exam creation
function validateQuestionAvailability() {
    const totalAvailable = questionsData.length;
    const totalRequested = parseInt(totalQuestionsInput.value) || 0;
    
    const validationMessage = document.getElementById('question-availability-message');
    if (!validationMessage) {
        const messageDiv = document.createElement('div');
        messageDiv.id = 'question-availability-message';
        messageDiv.className = 'validation-message';
        totalQuestionsInput.parentNode.appendChild(messageDiv);
    }
    
    const messageElement = document.getElementById('question-availability-message');
    
    if (totalAvailable < totalRequested) {
        messageElement.textContent = `Warning: Only ${totalAvailable} questions available in this subject. Please adjust your requirements.`;
        messageElement.className = 'validation-message error';
        return false;
    } else {
        messageElement.textContent = `${totalAvailable} questions available for this subject.`;
        messageElement.className = 'validation-message success';
        return true;
    }
}

// Enhanced slider validation with scope awareness
function validateSliderValues() {
    let isValid = true;
    const totalQuestions = parseInt(totalQuestionsInput.value) || 0;
    
    sliders.forEach(slider => {
        const requestedCount = parseInt(slider.range.value);
        const availableCount = parseInt(slider.range.getAttribute('data-available')) || 0;
        
        if (requestedCount > availableCount) {
            slider.range.style.borderColor = '#e53e3e';
            isValid = false;
        } else {
            slider.range.style.borderColor = '';
        }
    });
    
    return isValid && validateQuestionAvailability();
}

// Get selected subject from step 1 (if stored in sessionStorage or form)
function getSelectedSubjectFromStep1() {
    // Try to get from sessionStorage first
    let subjectId = sessionStorage.getItem('selectedSubjectId');
    
    if (!subjectId) {
        // Try to get from a hidden input or form field
        const subjectInput = document.getElementById('selected-subject-id');
        if (subjectInput) {
            subjectId = subjectInput.value;
        }
    }
    
    return subjectId;
}

// Initialize when page loads
document.addEventListener('DOMContentLoaded', function() {
    initializeSubjectScope();
});

