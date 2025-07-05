// HED Approve Questions JavaScript - Updated to use real data
document.addEventListener('DOMContentLoaded', function() {
    initializeApprovalPage();
});

let allExamsData = [];
let filteredExamsData = [];
let allQuestionsData = [];
let hedScope = {};

// Initialize page
async function initializeApprovalPage() {
    try {
        console.log('Initializing HED Approval page...');
        
        // Load exam data from API
        await loadExamData();
        
        // Load notifications
        await loadNotifications();
        
        // Setup event listeners
        setupEventListeners();
        
        // Setup filters
        setupFilters();
        
    } catch (error) {
        console.error('Error initializing page:', error);
        showErrorMessage('Failed to load page data');
    }
}

// Load exam data from backend API
async function loadExamData() {
    try {
        const response = await fetch('/api/hed/exams', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        allExamsData = data || [];
        filteredExamsData = [...allExamsData];
        
        console.log('Loaded exam data:', allExamsData);
        
        // Update table if we have data
        if (allExamsData.length > 0) {
            updateExamTable(allExamsData);
        }
        
    } catch (error) {
        console.error('Error loading exam data:', error);
        // Keep the Thymeleaf-rendered data as fallback
        console.log('Using server-side rendered data as fallback');
    }
}

// Load notifications from API
async function loadNotifications() {
    try {
        const response = await fetch('/api/hed/notifications', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const notifications = await response.json();
        updateNotifications(notifications);
          } catch (error) {
        console.error('Error loading notifications:', error);
        // Keep server-side rendered notifications
    }
}

// Load HED department scope information
async function loadHEDScope() {
    try {
        const response = await fetch('/api/hed/scope', {
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

// Create question approval element
function createQuestionApprovalElement(question) {
    const questionDiv = document.createElement('div');
    questionDiv.className = 'question-approval-item';
    questionDiv.innerHTML = `
        <div class="question-header">
            <span class="question-id">Q${question.id}</span>
            <span class="question-subject">${question.subjectName || 'N/A'}</span>
            <span class="question-difficulty ${question.difficultyLevel?.toLowerCase() || 'medium'}">${question.difficultyLevel || 'Medium'}</span>
        </div>
        <div class="question-content">
            <p class="question-text">${question.content || 'No content available'}</p>
            <div class="question-options">
                <div class="option">A: ${question.answerF1 || 'N/A'}</div>
                <div class="option">B: ${question.answerF2 || 'N/A'}</div>
                <div class="option">C: ${question.answerF3 || 'N/A'}</div>
                <div class="option correct">Correct: ${question.answerKey || 'N/A'}</div>
            </div>
            ${question.explanation ? `<div class="question-explanation"><strong>Explanation:</strong> ${question.explanation}</div>` : ''}
        </div>
        <div class="question-actions">
            <button class="action-btn approve-btn" onclick="approveQuestion(${question.id})">
                <i class="fas fa-check"></i> Approve
            </button>
            <button class="action-btn reject-btn" onclick="rejectQuestion(${question.id})">
                <i class="fas fa-times"></i> Reject
            </button>
            <button class="action-btn edit-btn" onclick="editQuestion(${question.id})">
                <i class="fas fa-edit"></i> Edit
            </button>
        </div>
    `;
    return questionDiv;
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

// Update exam table with data
function updateExamTable(exams) {
    const tableBody = document.getElementById('examTableBody');
    if (!tableBody) return;
    
    if (!exams || exams.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="7" style="text-align: center; padding: 2rem; color: #666;">
                    No exams requiring approval in your department scope
                </td>
            </tr>
        `;
        return;
    }
    
    const rows = exams.map(exam => `
        <tr data-exam-id="${exam.id}">
            <td>${exam.id}</td>
            <td>${exam.subjectName || 'N/A'}</td>
            <td>${exam.lecturerName || 'N/A'}</td>
            <td>${exam.totalQuestions || 0}</td>
            <td>${formatDate(exam.deadline)}</td>
            <td>
                <span class="status-badge ${exam.status?.toLowerCase() || 'pending'}">${exam.status || 'Pending'}</span>
            </td>
            <td>
                <button class="action-btn view-btn" title="View" onclick="viewExam(${exam.id})">
                    <i class="fas fa-eye"></i>
                </button>
                ${exam.status === 'PENDING' ? `
                    <button class="action-btn edit-btn" title="Approve" onclick="approveExam(${exam.id})">
                        <i class="fas fa-check"></i>
                    </button>
                    <button class="action-btn delete-btn" title="Reject" onclick="rejectExam(${exam.id})">
                        <i class="fas fa-times"></i>
                    </button>
                ` : ''}
            </td>
        </tr>
    `).join('');
    
    tableBody.innerHTML = rows;
}

// Update notifications
function updateNotifications(notifications) {
    const container = document.getElementById('notificationContainer');
    if (!container) return;
    
    if (!notifications || notifications.length === 0) {
        container.innerHTML = `
            <div class="notification_item">
                <div class="notification-text">
                    <strong>No pending approvals at this time</strong>
                    <p>All submissions have been processed</p>
                </div>
            </div>
        `;
        return;
    }
    
    const notificationHtml = notifications.map(notif => `
        <div class="notification_item">
            <input type="checkbox" checked>
            <div class="notification-text">
                <strong>${notif.title || 'New notification'}</strong>
                <p>At: ${formatDateTime(notif.createdAt)}</p>
            </div>
        </div>
    `).join('');
    
    container.innerHTML = notificationHtml;
}

// Setup event listeners
function setupEventListeners() {
    // Search functionality
    const searchInput = document.querySelector('.search-input');
    if (searchInput) {
        searchInput.addEventListener('input', handleSearch);
    }
    
    // Filter dropdowns
    const statusFilter = document.querySelector('.filter-select[value=""]');
    const subjectFilter = document.querySelectorAll('.filter-select')[1];
    
    if (statusFilter) {
        statusFilter.addEventListener('change', applyFilters);
    }
    if (subjectFilter) {
        subjectFilter.addEventListener('change', applyFilters);
    }
    
    // Search button
    const searchBtn = document.querySelector('.search-btn');
    if (searchBtn) {
        searchBtn.addEventListener('click', handleSearch);
    }
}

// Setup filters with dynamic data
function setupFilters() {
    if (allExamsData.length === 0) return;
    
    // Get unique subjects for subject filter
    const subjects = [...new Set(allExamsData.map(exam => exam.subjectName).filter(Boolean))];
    const subjectFilter = document.querySelectorAll('.filter-select')[1];
    
    if (subjectFilter && subjects.length > 0) {
        const currentValue = subjectFilter.value;
        subjectFilter.innerHTML = `
            <option value="">All Subject</option>
            ${subjects.map(subject => 
                `<option value="${subject}" ${currentValue === subject ? 'selected' : ''}>${subject}</option>`
            ).join('')}
        `;
    }
}

// Handle search
function handleSearch() {
    const searchTerm = document.querySelector('.search-input')?.value?.toLowerCase() || '';
    
    let filtered = [...allExamsData];
    
    if (searchTerm) {
        filtered = filtered.filter(exam => 
            exam.subjectName?.toLowerCase().includes(searchTerm) ||
            exam.lecturerName?.toLowerCase().includes(searchTerm) ||
            exam.id?.toString().includes(searchTerm)
        );
    }
    
    filteredExamsData = filtered;
    applyFilters();
}

// Apply filters
function applyFilters() {
    const statusFilter = document.querySelector('.filter-select');
    const subjectFilter = document.querySelectorAll('.filter-select')[1];
    
    const statusValue = statusFilter?.value || '';
    const subjectValue = subjectFilter?.value || '';
    
    let filtered = [...filteredExamsData];
    
    if (statusValue) {
        filtered = filtered.filter(exam => 
            exam.status?.toLowerCase() === statusValue.toLowerCase()
        );
    }
    
    if (subjectValue) {
        filtered = filtered.filter(exam => 
            exam.subjectName === subjectValue
        );
    }
    
    updateExamTable(filtered);
}

// Action functions
function viewExam(examId) {
    console.log('Viewing exam:', examId);
    window.location.href = `/api/hed/exams/${examId}/details`;
}

function approveExam(examId) {
    if (confirm('Are you sure you want to approve this exam?')) {
        updateExamStatus(examId, 'APPROVED');
    }
}

function rejectExam(examId) {
    const reason = prompt('Please provide a reason for rejection:');
    if (reason) {
        updateExamStatus(examId, 'REJECTED', reason);
    }
}

// Update exam status
async function updateExamStatus(examId, status, reason = '') {
    try {
        const response = await fetch(`/api/hed/exams/${examId}/status`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                status: status,
                reason: reason
            })
        });
        
        if (response.ok) {
            alert(`Exam ${status.toLowerCase()} successfully!`);
            // Reload data
            await loadExamData();
            await loadNotifications();
        } else {
            throw new Error('Failed to update status');
        }
    } catch (error) {
        console.error('Error updating exam status:', error);
        alert('Failed to update exam status. Please try again.');
    }
}

// Load more notifications
function loadMoreNotifications() {
    console.log('Loading more notifications...');
    // Implement pagination if needed
}

// Question approval actions
function approveQuestion(questionId) {
    if (confirm('Are you sure you want to approve this question?')) {
        updateQuestionStatus(questionId, 'APPROVED');
    }
}

function rejectQuestion(questionId) {
    const reason = prompt('Please provide a reason for rejection:');
    if (reason) {
        updateQuestionStatus(questionId, 'REJECTED', reason);
    }
}

function editQuestion(questionId) {
    console.log('Editing question:', questionId);
    window.location.href = `/api/questions/${questionId}/edit`;
}

// Update question status
async function updateQuestionStatus(questionId, status, reason = '') {
    try {
        const response = await fetch(`/api/hed/questions/${questionId}/status`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                status: status,
                reason: reason
            })
        });
        
        if (response.ok) {
            alert(`Question ${status.toLowerCase()} successfully!`);
            // Reload data
            await loadQuestionDataWithScope();
        } else {
            throw new Error('Failed to update question status');
        }
    } catch (error) {
        console.error('Error updating question status:', error);
        alert('Failed to update question status. Please try again.');
    }
}

// Utility functions
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-GB', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
}

function formatDateTime(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleString('en-GB', {
        hour: '2-digit',
        minute: '2-digit',
        hour12: true,
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
}

function showErrorMessage(message) {
    console.error(message);
    // You can implement a toast or modal here
}

// Get CSS class for status badge
function getStatusClass(status) {
    if (!status) return 'pending';
    
    switch (status.toLowerCase()) {
        case 'approved':
            return 'approved';
        case 'rejected':
            return 'rejected';
        case 'pending':
        case 'submitted':
            return 'pending';
        case 'in_progress':
        case 'in-progress':
            return 'in-progress';
        default:
            return 'pending';
    }
}
