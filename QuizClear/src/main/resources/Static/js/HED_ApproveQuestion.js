// HED Approve Questions JavaScript with Department Scope Filtering
document.addEventListener('DOMContentLoaded', function() {
    // Initialize page with scope awareness
    initializeHEDApproval();
});

// Global scope variables for HED (Head of Examination Department)
let hedScope = {
    userId: null,
    userRole: 'HED',
    managedDepartmentIds: [],
    accessibleSubjectIds: [],
    canApproveAll: false,
    departmentName: ''
};

// All questions/exams data for filtering
let allQuestionsData = [];
let allExamsData = [];

// Initialize HED approval page with department scope
async function initializeHEDApproval() {
    try {
        console.log('=== Initializing HED Approval with department scope ===');
        
        // Load HED scope first
        await loadHEDScope();
        
        // Load data filtered by department scope
        await loadDataWithDepartmentScope();
        
        // Setup event listeners
        setupEventListeners();
        
    } catch (error) {
        console.error('Error initializing HED approval:', error);
        loadFallbackData();
    }
}

// Load HED's department scope
async function loadHEDScope() {
    try {
        const response = await fetch('/api/user/current-scope', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            hedScope = await response.json();
            console.log('HED scope loaded:', hedScope);
            
            // Update scope indicator
            updateDepartmentScopeIndicator();
        } else {
            console.error('Failed to load HED scope');
        }
    } catch (error) {
        console.error('Error loading HED scope:', error);
    }
}

// Load exam and question data filtered by department scope
async function loadDataWithDepartmentScope() {
    console.log('=== Loading data with department scope filtering ===');
    
    await Promise.all([
        loadExamDataWithScope(),
        loadQuestionDataWithScope()
    ]);
}

// Load exam data filtered by HED's department scope
async function loadExamDataWithScope() {
    try {
        const params = new URLSearchParams();
        if (hedScope.managedDepartmentIds.length > 0) {
            params.append('departmentIds', hedScope.managedDepartmentIds.join(','));
        }
        if (hedScope.accessibleSubjectIds.length > 0) {
            params.append('subjectIds', hedScope.accessibleSubjectIds.join(','));
        }
        params.append('requestingUserId', hedScope.userId);
        params.append('userRole', hedScope.userRole);
        params.append('requiresApproval', 'true'); // Only exams needing HED approval

        const response = await fetch(`/api/hed/exams?${params}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            allExamsData = await response.json();
            console.log(`Loaded ${allExamsData.length} exams for department scope`);
            populateExamTable(allExamsData);
        } else {
            throw new Error('Failed to load department-scoped exam data: ' + response.status);
        }
    } catch (error) {
        console.error('Error loading department-scoped exams:', error);
        showErrorMessage('Failed to load exam data from server');
    }
}

// Load question data filtered by HED's department scope
async function loadQuestionDataWithScope() {
    try {
        const params = new URLSearchParams();
        if (hedScope.managedDepartmentIds.length > 0) {
            params.append('departmentIds', hedScope.managedDepartmentIds.join(','));
        }
        if (hedScope.accessibleSubjectIds.length > 0) {
            params.append('subjectIds', hedScope.accessibleSubjectIds.join(','));
        }
        params.append('requestingUserId', hedScope.userId);
        params.append('userRole', hedScope.userRole);
        params.append('status', 'PENDING_HED_APPROVAL'); // Only questions needing HED approval

        const response = await fetch(`/api/questions/for-hed-approval?${params}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            allQuestionsData = await response.json();
            console.log(`Loaded ${allQuestionsData.length} questions for HED approval`);
            
            // Update questions section if exists
            updateQuestionApprovalSection();
        } else {
            console.error('Failed to load questions for HED approval');
        }
    } catch (error) {
        console.error('Error loading questions for HED approval:', error);
    }
}

// Update department scope indicator
function updateDepartmentScopeIndicator() {
    let scopeIndicator = document.getElementById('hed-scope-indicator');
    if (!scopeIndicator) {
        scopeIndicator = document.createElement('div');
        scopeIndicator.id = 'hed-scope-indicator';
        scopeIndicator.className = 'scope-indicator-hed';
        
        const header = document.querySelector('.page-header') || document.querySelector('h1');
        if (header) {
            header.parentNode.insertBefore(scopeIndicator, header.nextSibling);
        }
    }
    
    const deptCount = hedScope.managedDepartmentIds.length;
    const subjCount = hedScope.accessibleSubjectIds.length;
    
    if (hedScope.canApproveAll) {
        scopeIndicator.innerHTML = `
            <div class="scope-info all-departments">
                <span class="scope-label">All Departments Access</span>
                <span class="scope-details">You can approve content from all departments</span>
            </div>
        `;
    } else if (deptCount > 0) {
        scopeIndicator.innerHTML = `
            <div class="scope-info department-scope">
                <span class="scope-label">Department Scope:</span>
                <span class="scope-details">${hedScope.departmentName || `${deptCount} departments`} - ${subjCount} subjects</span>
            </div>
        `;
    } else {
        scopeIndicator.innerHTML = `
            <div class="scope-info no-scope">
                <span class="scope-label">No Department Assigned</span>
                <span class="scope-details">Contact administrator for department assignment</span>
            </div>
        `;
    }
}

// Enhanced populate exam table with scope awareness
function populateExamTable(exams) {
    const tbody = document.querySelector('.exam-table tbody');
    if (!tbody) return;

    tbody.innerHTML = '';

    // Filter exams by department scope
    const filteredExams = exams.filter(exam => isExamInDepartmentScope(exam));

    if (filteredExams.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" class="no-data">
                    No exams requiring approval in your department scope
                </td>
            </tr>
        `;
        return;
    }

    filteredExams.forEach(exam => {
        const row = document.createElement('tr');
        row.className = exam.isWithinScope ? '' : 'out-of-scope';
        row.innerHTML = `
            <td>A${exam.examId.toString().padStart(4, '0')}</td>
            <td>
                ${exam.courseName}
                ${exam.departmentName ? `<br><small class="department-name">${exam.departmentName}</small>` : ''}
            </td>
            <td>${exam.createdBy}</td>
            <td>${exam.totalQuestions || 'N/A'}</td>
            <td>${formatDate(exam.examDate)}</td>
            <td><span class="status-badge ${getStatusClass(exam.reviewStatus)}">${exam.reviewStatus}</span></td>
            <td>
                <button class="action-btn view-btn" title="View" onclick="viewExamDetails(${exam.examId})" 
                        ${!exam.canView ? 'disabled' : ''}>
                    <i class="fas fa-eye"></i>
                </button>
                ${exam.reviewStatus === 'PENDING' && exam.canApprove ? `
                    <button class="action-btn approve-btn" title="Approve" onclick="approveExam(${exam.examId})">
                        <i class="fas fa-check"></i>
                    </button>
                    <button class="action-btn reject-btn" title="Reject" onclick="rejectExam(${exam.examId})">
                        <i class="fas fa-times"></i>
                    </button>
                ` : ''}
                ${exam.reviewStatus === 'PENDING' && exam.canEdit ? `
                    <button class="action-btn edit-btn" title="Edit" onclick="editExam(${exam.examId})">
                        <i class="fas fa-edit"></i>
                    </button>
                ` : ''}
            </td>
        `;
        tbody.appendChild(row);
    });

    // Update summary
    updateApprovalSummary(filteredExams);
}

// Update question approval section
function updateQuestionApprovalSection() {
    const questionContainer = document.getElementById('questions-for-approval');
    if (!questionContainer) return;

    const filteredQuestions = allQuestionsData.filter(question => isQuestionInDepartmentScope(question));

    if (filteredQuestions.length === 0) {
        questionContainer.innerHTML = `
            <div class="no-questions">
                <p>No questions requiring approval in your department scope</p>
            </div>
        `;
        return;
    }

    questionContainer.innerHTML = '';
    filteredQuestions.forEach(question => {
        const questionElement = createQuestionApprovalElement(question);
        questionContainer.appendChild(questionElement);
    });
}

// Check if exam is within HED's department scope
function isExamInDepartmentScope(exam) {
    if (hedScope.canApproveAll) return true;

    // Check department scope
    if (hedScope.managedDepartmentIds.length > 0 && exam.departmentId) {
        if (!hedScope.managedDepartmentIds.includes(exam.departmentId)) {
            return false;
        }
    }

    // Check subject scope
    if (hedScope.accessibleSubjectIds.length > 0 && exam.subjectId) {
        if (!hedScope.accessibleSubjectIds.includes(exam.subjectId)) {
            return false;
        }
    }

    return true;
}

// Check if question is within HED's department scope
function isQuestionInDepartmentScope(question) {
    if (hedScope.canApproveAll) return true;

    // Check department scope
    if (hedScope.managedDepartmentIds.length > 0 && question.departmentId) {
        if (!hedScope.managedDepartmentIds.includes(question.departmentId)) {
            return false;
        }
    }

    // Check subject scope
    if (hedScope.accessibleSubjectIds.length > 0 && question.subjectId) {
        if (!hedScope.accessibleSubjectIds.includes(question.subjectId)) {
            return false;
        }
    }

    return true;
}

// Enhanced filtering with department scope
function filterExamsByDepartment(departmentId) {
    let filteredExams = allExamsData;

    if (departmentId && departmentId !== 'all') {
        filteredExams = filteredExams.filter(exam => exam.departmentId == departmentId);
    }

    // Apply scope filtering
    filteredExams = filteredExams.filter(exam => isExamInDepartmentScope(exam));

    populateExamTable(filteredExams);
}

// Update approval summary with scope information
function updateApprovalSummary(exams) {
    const summaryContainer = document.getElementById('approval-summary');
    if (!summaryContainer) return;

    const pending = exams.filter(e => e.reviewStatus === 'PENDING').length;
    const approved = exams.filter(e => e.reviewStatus === 'APPROVED').length;
    const rejected = exams.filter(e => e.reviewStatus === 'REJECTED').length;

    summaryContainer.innerHTML = `
        <div class="summary-stats">
            <div class="stat-item">
                <span class="stat-label">Pending Approval:</span>
                <span class="stat-value pending">${pending}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Approved:</span>
                <span class="stat-value approved">${approved}</span>
            </div>
            <div class="stat-item">
                <span class="stat-label">Rejected:</span>
                <span class="stat-value rejected">${rejected}</span>
            </div>
        </div>
        ${!hedScope.canApproveAll ? `
            <div class="scope-note">
                <small>Showing results for your department scope only</small>
            </div>
        ` : ''}
    `;
}

// Enhanced approval actions with scope validation
async function approveExam(examId) {
    const exam = allExamsData.find(e => e.examId === examId);
    if (!exam || !isExamInDepartmentScope(exam)) {
        alert('You do not have permission to approve this exam');
        return;
    }

    try {
        const response = await fetch(`/api/hed/exams/${examId}/approve`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                approverId: hedScope.userId,
                departmentValidated: true
            })
        });

        if (response.ok) {
            alert('Exam approved successfully');
            await loadExamDataWithScope(); // Reload data
        } else {
            alert('Failed to approve exam');
        }
    } catch (error) {
        console.error('Error approving exam:', error);
        alert('Error approving exam');
    }
}

// Enhanced rejection with scope validation
async function rejectExam(examId) {
    const exam = allExamsData.find(e => e.examId === examId);
    if (!exam || !isExamInDepartmentScope(exam)) {
        alert('You do not have permission to reject this exam');
        return;
    }

    const reason = prompt('Please provide a reason for rejection:');
    if (!reason) return;

    try {
        const response = await fetch(`/api/hed/exams/${examId}/reject`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                rejectedBy: hedScope.userId,
                rejectionReason: reason,
                departmentValidated: true
            })
        });

        if (response.ok) {
            alert('Exam rejected successfully');
            await loadExamDataWithScope(); // Reload data
        } else {
            alert('Failed to reject exam');
        }
    } catch (error) {
        console.error('Error rejecting exam:', error);
        alert('Error rejecting exam');
    }
}

// Fallback data when API fails
function loadFallbackData() {
    console.log('Loading fallback data for HED approval');
    populateExamTable([]);
}

// Backward compatibility - keeping original function name
async function loadExamData() {
    await loadExamDataWithScope();
}
