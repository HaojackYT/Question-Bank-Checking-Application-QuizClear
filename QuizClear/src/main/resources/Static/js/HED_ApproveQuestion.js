// HED Approve Questions JavaScript
document.addEventListener('DOMContentLoaded', function() {
    // Initialize page
    loadExamData();
    setupEventListeners();
});

// Load exam data from backend
async function loadExamData() {    try {
        const response = await fetch('/api/hed/exams');
        if (response.ok) {
            const exams = await response.json();
            populateExamTable(exams);
        } else {
            console.error('Failed to load exam data');
            // Show error message instead of mock data
            showErrorMessage('Failed to load exam data from server');
        }
    } catch (error) {
        console.error('Error loading exam data:', error);
        showErrorMessage('Error connecting to server: ' + error.message);
    }
}



// Populate the exam table with data
function populateExamTable(exams) {
    const tbody = document.querySelector('.exam-table tbody');
    if (!tbody) return;

    tbody.innerHTML = '';

    exams.forEach(exam => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>A${exam.examId.toString().padStart(4, '0')}</td>
            <td>${exam.courseName}</td>
            <td>${exam.createdBy}</td>
            <td>${exam.totalQuestions || 'N/A'}</td>
            <td>${formatDate(exam.examDate)}</td>            <td><span class="status-badge ${getStatusClass(exam.reviewStatus)}">${exam.reviewStatus}</span></td>
            <td>
                <button class="action-btn view-btn" title="View" onclick="viewExamDetails(${exam.examId})">
                    <i class="fas fa-eye"></i>
                </button>
                ${exam.reviewStatus === 'PENDING' ? `
                    <button class="action-btn edit-btn" title="Edit" onclick="editExam(${exam.examId})">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="action-btn delete-btn" title="Delete" onclick="deleteExam(${exam.examId})">
                        <i class="fas fa-trash"></i>
                    </button>
                ` : ''}
            </td>
        `;
        tbody.appendChild(row);
    });
}

// Format date for display
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    try {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-GB');
    } catch (error) {
        return dateString;
    }
}

// Get CSS class for status
function getStatusClass(status) {
    if (!status) {
        return 'pending'; // Default fallback
    }
    switch (status.toLowerCase()) {
        case 'approved': return 'approved';
        case 'pending': return 'pending';
        case 'rejected': return 'rejected';
        case 'in progress': return 'in-progress';
        case 'in_progress': return 'in-progress';
        default: return 'pending';
    }
}

// Setup event listeners
function setupEventListeners() {
    // Search functionality
    const searchBtn = document.querySelector('.search-btn');
    const searchInput = document.querySelector('.search-input');
    
    if (searchBtn && searchInput) {
        searchBtn.addEventListener('click', performSearch);
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                performSearch();
            }
        });
    }

    // Filter functionality
    const filterSelects = document.querySelectorAll('.filter-select');
    filterSelects.forEach(select => {
        select.addEventListener('change', performSearch);
    });
}

// Perform search and filter
async function performSearch() {
    const searchQuery = document.querySelector('.search-input')?.value || '';
    const statusFilter = document.querySelector('.filter-select[option="approved"]')?.parentElement?.querySelector('select')?.value || '';
    const subjectFilter = document.querySelector('.filter-select')?.parentElement?.nextElementSibling?.querySelector('select')?.value || '';

    try {
        const response = await fetch(`/api/hed/exams/search?query=${encodeURIComponent(searchQuery)}&status=${encodeURIComponent(statusFilter)}&subject=${encodeURIComponent(subjectFilter)}`);
        if (response.ok) {
            const exams = await response.json();
            populateExamTable(exams);
        }
    } catch (error) {
        console.error('Error searching exams:', error);
    }
}

// View exam details
async function viewExamDetails(examId) {
    try {
        const response = await fetch(`/api/hed/exam/${examId}`);
        if (response.ok) {
            const exam = await response.json();
            openExamModal(exam);
        }
    } catch (error) {
        console.error('Error loading exam details:', error);
        // Show modal with basic info
        openExamModal({ examId, examTitle: "Exam Details", courseName: "Loading..." });
    }
}

// Open exam details modal
function openExamModal(exam) {
    const modal = document.getElementById('examDetailsModal');
    if (!modal) return;

    // Populate modal with exam data
    document.getElementById('modalLecturer').textContent = exam.createdBy || 'N/A';
    document.getElementById('modalTitle').textContent = exam.examTitle || 'N/A';
    document.getElementById('modalTotalQuestion').textContent = exam.totalQuestions || 'N/A';
    document.getElementById('modalSubject').textContent = exam.courseName || 'N/A';
    document.getElementById('modalDeadline').textContent = formatDate(exam.dueDate);
    document.getElementById('modalDueDate').textContent = formatDate(exam.dueDate);
    
    const statusElement = document.getElementById('modalStatus');
    if (statusElement) {
        statusElement.textContent = exam.reviewStatus?.displayName || 'N/A';
        statusElement.className = `status-badge ${getStatusClass(exam.reviewStatus?.displayName || '')}`;
    }

    document.getElementById('modalDescription').textContent = exam.description || 'This is a detailed description of the content that needs to be approved.';

    modal.style.display = 'block';

    // Setup modal buttons
    setupModalButtons(exam.examId);
}

// Setup modal buttons
function setupModalButtons(examId) {
    const approveBtn = document.querySelector('.approve-btn');
    const rejectBtn = document.querySelector('.reject-btn');
    const closeBtn = document.querySelector('.close-btn');

    if (approveBtn) {
        approveBtn.onclick = () => approveExam(examId);
    }
    
    if (rejectBtn) {
        rejectBtn.onclick = () => rejectExam(examId);
    }

    if (closeBtn) {
        closeBtn.onclick = () => closeModal();
    }
}

// Approve exam
async function approveExam(examId) {
    const feedback = document.getElementById('modalFeedback')?.value || '';
    
    try {
        const response = await fetch(`/api/hed/exam/${examId}/approve`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(feedback)
        });

        if (response.ok) {
            alert('Exam approved successfully!');
            closeModal();
            loadExamData(); // Refresh the table
        } else {
            alert('Failed to approve exam');
        }
    } catch (error) {
        console.error('Error approving exam:', error);
        alert('Error approving exam');
    }
}

// Reject exam
async function rejectExam(examId) {
    const feedback = document.getElementById('modalFeedback')?.value || '';
    
    if (!feedback.trim()) {
        alert('Please provide feedback before rejecting the exam.');
        return;
    }

    try {
        const response = await fetch(`/api/hed/exam/${examId}/reject`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(feedback)
        });

        if (response.ok) {
            alert('Exam rejected with feedback.');
            closeModal();
            loadExamData(); // Refresh the table
        } else {
            alert('Failed to reject exam');
        }
    } catch (error) {
        console.error('Error rejecting exam:', error);
        alert('Error rejecting exam');
    }
}

// Close modal
function closeModal() {
    const modal = document.getElementById('examDetailsModal');
    if (modal) {
        modal.style.display = 'none';
    }
}

// Edit exam
function editExam(examId) {
    alert(`Edit functionality for exam ${examId} - To be implemented`);
}

// Delete exam
function deleteExam(examId) {
    if (confirm('Are you sure you want to delete this exam?')) {
        alert(`Delete functionality for exam ${examId} - To be implemented`);
    }
}

// Close modal when clicking outside
window.addEventListener('click', function(event) {
    const modal = document.getElementById('examDetailsModal');
    if (event.target === modal) {
        closeModal();
    }
});

// Show error message when data loading fails
function showErrorMessage(message) {
    const tbody = document.querySelector('.exam-table tbody');
    tbody.innerHTML = `
        <tr>
            <td colspan="7" class="text-center text-danger">
                <i class="fas fa-exclamation-triangle"></i> ${message}
            </td>
        </tr>
    `;
}
