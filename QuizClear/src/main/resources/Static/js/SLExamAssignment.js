// SL Exam Assignment JavaScript
// Handles all frontend interactions for Subject Leader Exam Assignment functionality

// Global variables
let allAssignmentsData = [];
let availableLecturers = [];
let availableCourses = [];

// DOM elements
let modalOverlay, newAssignModal, detailsModalOverlay, detailsModal, searchInput;

// Initialize page
function initializePage() {
    console.log('=== Initializing SL Exam Assignment ===');
    
    // Get DOM elements
    modalOverlay = document.getElementById('newAssignModalOverlay');
    newAssignModal = document.getElementById('newAssignModal');
    detailsModalOverlay = document.getElementById('detailsModalOverlay');
    detailsModal = document.getElementById('detailsModal');
    searchInput = document.querySelector('.search-input');
    
    // Load assignments data
    loadAssignments();
    
    // Setup event listeners
    setupEventListeners();
    
    // Load lecturers for dropdown
    loadLecturers();
}

// Setup event listeners
function setupEventListeners() {
    // Search functionality
    if (searchInput) {
        searchInput.addEventListener('input', debounce(performSearch, 300));
    }
    
    // Status filter
    const statusFilter = document.getElementById('statusFilter');
    if (statusFilter) {
        statusFilter.addEventListener('change', loadAssignments);
    }
    
    // New assign button
    const newAssignBtn = document.getElementById('newAssignBtn');
    if (newAssignBtn) {
        newAssignBtn.addEventListener('click', openNewAssignModal);
    }
    
    // Assign button in modal
    const assignButton = document.querySelector('.assign-button');
    if (assignButton) {
        assignButton.addEventListener('click', createAssignment);
    }
    
    // Lecturer dropdown change event
    const lecturerSelect = document.getElementById('lecturer');
    if (lecturerSelect) {
        lecturerSelect.addEventListener('change', onLecturerChange);
    }
    
    // Modal close buttons
    document.addEventListener('click', (e) => {
        if (e.target.classList.contains('close-modal-btn') || 
            e.target.classList.contains('close-details-modal-btn') ||
            e.target.classList.contains('modal-overlay')) {
            closeAllModals();
        }
    });
}

// Debounce function for search
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

    // Close modals with ESC key
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            closeAllModals();
        }
    });

    // Modal functions
    function openNewAssignModal() {
        closeAllModals();
        clearNewAssignForm();
        if (modalOverlay && newAssignModal) {
            modalOverlay.style.display = 'block';
            newAssignModal.style.display = 'block';
            document.body.classList.add('modal-open');
        }
    }

    function closeAllModals() {
        if (modalOverlay && newAssignModal) {
            modalOverlay.style.display = 'none';
            newAssignModal.style.display = 'none';
        }
        
        if (detailsModalOverlay && detailsModal) {
            detailsModalOverlay.style.display = 'none';
            detailsModal.style.display = 'none';
        }
        
        document.body.classList.remove('modal-open');
    }

    function clearNewAssignForm() {
        document.getElementById('examTitle').value = '';
        document.getElementById('lecturer').value = '';
        document.getElementById('course').value = '';
        document.getElementById('noOfExams').value = '';
        document.getElementById('examType').value = '';
        document.getElementById('assignDate').value = '';
        document.getElementById('dueDate').value = '';
        const note = document.getElementById('note');
        if (note) note.value = '';

        // Clear course dropdown
        const courseSelect = document.getElementById('course');
        courseSelect.innerHTML = '<option value="">Select Course</option>';
    }

    async function loadAssignments() {
        try {
            const statusFilter = document.getElementById('statusFilter');
            const params = new URLSearchParams();
            
            if (statusFilter?.value) {
                params.append('status', statusFilter.value);
            }
            
            const response = await fetch(`/subject-leader/exam-assignments/api/assignments?${params}`);
            if (response.ok) {
                const data = await response.json();
                renderAssignmentsTable(data.content || data);
            } else {
                throw new Error('Failed to load assignments');
            }
        } catch (error) {
            console.error('Error loading assignments:', error);
            showNotification('Error loading assignments', 'error');
        }
    }

    function renderAssignmentsTable(assignments) {
        const tbody = document.getElementById('assignmentTableBody');
        if (!tbody) return;

        if (!assignments || assignments.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="7" style="text-align: center; padding: 20px;">
                        No assignments found. Click "New assign" to create your first assignment.
                    </td>
                </tr>
            `;
            return;
        }

        tbody.innerHTML = assignments.map((assignment, index) => {
            // Chỉ hiển thị icon edit/xóa nếu trạng thái là Draft
            const isDraft = assignment.status === 'DRAFT' || assignment.statusDisplayName === 'Draft - Not Assigned Yet';
            return `
                <tr>
                    <td>${index + 1}</td>
                    <td>${assignment.assignmentName}</td>
                    <td>${assignment.courseCode} - ${assignment.courseName}</td>
                    <td>${assignment.assignedToName}</td>
                    <td>${formatDate(assignment.deadline)}</td>
                    <td>
                        <span class="status ${getStatusClass(assignment.status)}">${assignment.statusDisplayName}</span>
                    </td>
                    <td class="actions">
                        <button class="action-icon view-icon" onclick="viewAssignment(${assignment.assignmentId})">
                            <i class="fa-solid fa-eye"></i>
                        </button>
                        ${isDraft ? `
                            <button class="action-icon edit-icon" onclick="editAssignment(${assignment.assignmentId})">
                                <i class="fa-solid fa-pen-to-square"></i>
                            </button>
                            <button class="action-icon delete-icon" onclick="deleteAssignment(${assignment.assignmentId})">
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        ` : ''}
                    </td>
                </tr>
            `;
        }).join('');
    }

    function getStatusClass(status) {
        const statusMap = {
            'DRAFT': 'draft',
            'ASSIGNED': 'assigned', 
            'IN_PROGRESS': 'in-progress',
            'SUBMITTED': 'submitted',
            'APPROVED': 'approved',
            'REJECTED': 'rejected',
            'PUBLISHED': 'published'
        };
        return statusMap[status] || 'unknown';
    }

    function formatDate(dateString) {
        if (!dateString) return 'No deadline';
        const date = new Date(dateString);
        return date.toLocaleDateString('en-GB');
    }    function openNewAssignModal() {
        // Close any other open modals first
        closeAllModals();
        
        clearNewAssignForm();
        modalOverlay.style.display = 'block';
        newAssignModal.style.display = 'block';
        document.body.classList.add('modal-open');
    }

    function closeAllModals() {
        // Close New Assign Modal
        if (modalOverlay && newAssignModal) {
            modalOverlay.style.display = 'none';
            newAssignModal.style.display = 'none';
        }
        
        // Close Details Modal
        if (detailsModalOverlay && detailsModal) {
            detailsModalOverlay.style.display = 'none';
            detailsModal.style.display = 'none';
        }
        
        // Remove modal-open class from body
        document.body.classList.remove('modal-open');
    }

    function closeNewAssignModal() {
        modalOverlay.style.display = 'none';
        newAssignModal.style.display = 'none';
        document.body.classList.remove('modal-open');
        clearNewAssignForm();
    }

    function closeDetailsModal() {
        detailsModalOverlay.style.display = 'none';
        detailsModal.style.display = 'none';
        document.body.classList.remove('modal-open');
    }

    function clearNewAssignForm() {
        document.getElementById('examTitle').value = '';
        document.getElementById('lecturer').value = '';
        document.getElementById('course').value = '';
        document.getElementById('noOfExams').value = '';
        document.getElementById('examType').value = '';
        document.getElementById('assignDate').value = '';
        document.getElementById('dueDate').value = '';
        const note = document.getElementById('note');
        if (note) note.value = '';
    }

    async function createAssignment() {
        try {
            const formData = {
                assignmentName: document.getElementById('examTitle').value,
                description: 'Exam assignment created via Subject Leader dashboard',
                courseId: parseInt(document.getElementById('course').value),
                assignedToId: parseInt(document.getElementById('lecturer').value),
                deadline: parseDateInput(document.getElementById('dueDate').value),
                totalQuestions: parseInt(document.getElementById('noOfExams').value) || 20,
                durationMinutes: 90,
                instructions: document.getElementById('structure').value + ' format exam'
            };

            if (!validateAssignmentForm(formData)) {
                return;
            }

            const response = await fetch('/subject-leader/exam-assignments/api/assignments', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                const result = await response.json();
                showNotification('Assignment created successfully', 'success');
                closeAllModals();
                loadAssignments();
            } else {
                const error = await response.json();
                showNotification(error.error || 'Failed to create assignment', 'error');
            }
        } catch (error) {
            console.error('Error creating assignment:', error);
            showNotification('Error creating assignment', 'error');
        }
    }

    function validateAssignmentForm(formData) {
        if (!formData.assignmentName?.trim()) {
            showNotification('Assignment name is required', 'error');
            return false;
        }
        if (!formData.courseId) {
            showNotification('Please select a course', 'error');
            return false;
        }
        if (!formData.assignedToId) {
            showNotification('Please select a lecturer', 'error');
            return false;
        }
        return true;
    }

    function parseDateInput(dateString) {
        if (!dateString) return null;
        try {
            // Assuming format DD/MM/YY or DD/MM/YYYY
            const parts = dateString.split('/');
            if (parts.length === 3) {
                let [day, month, year] = parts;
                if (year.length === 2) {
                    year = '20' + year;
                }
                const date = new Date(parseInt(year), parseInt(month) - 1, parseInt(day));
                return date.toISOString();
            }
        } catch (e) {
            console.error('Error parsing date:', e);
        }
        return null;
    }

    async function performSearch() {
        const searchTerm = searchInput?.value?.trim();
        if (!searchTerm) {
            loadAssignments();
            return;
        }

        try {
            const response = await fetch(`/subject-leader/exam-assignments/api/assignments/search?searchTerm=${encodeURIComponent(searchTerm)}`);
            if (response.ok) {
                const assignments = await response.json();
                renderAssignmentsTable(assignments);
            }
        } catch (error) {
            console.error('Error searching assignments:', error);
            showNotification('Error searching assignments', 'error');
        }
    }

    // Load lecturers from database
    async function loadLecturers() {
        try {
            const response = await fetch('/subject-leader/exam-assignments/api/lecturers');
            if (response.ok) {
                availableLecturers = await response.json();
                populateLecturerDropdown();
            } else {
                console.error('Failed to load lecturers');
                showNotification('Failed to load lecturers', 'error');
            }
        } catch (error) {
            console.error('Error loading lecturers:', error);
            showNotification('Error loading lecturers', 'error');
        }
    }
    
    // Populate lecturer dropdown
    function populateLecturerDropdown() {
        const lecturerSelect = document.getElementById('lecturer');
        if (!lecturerSelect) return;
        
        lecturerSelect.innerHTML = '<option value="">Select Lecturer</option>';
        
        availableLecturers.forEach(lecturer => {
            const option = document.createElement('option');
            option.value = lecturer.userId;
            option.textContent = lecturer.fullName;
            lecturerSelect.appendChild(option);
        });
    }
    
    // Handle lecturer selection change
    async function onLecturerChange() {
        const lecturerSelect = document.getElementById('lecturer');
        const courseSelect = document.getElementById('course');
        const lecturerId = lecturerSelect.value;
        
        // Clear course dropdown
        courseSelect.innerHTML = '<option value="">Select Course</option>';
        
        if (!lecturerId) {
            return;
        }
        
        try {
            const response = await fetch(`/subject-leader/exam-assignments/api/courses/by-lecturer/${lecturerId}`);
            if (response.ok) {
                const courses = await response.json();
                populateCourseDropdown(courses);
            } else {
                console.error('Failed to load courses for lecturer');
                showNotification('Failed to load courses for selected lecturer', 'error');
            }
        } catch (error) {
            console.error('Error loading courses for lecturer:', error);
            showNotification('Error loading courses for lecturer', 'error');
        }
    }
    
    // Populate course dropdown
    function populateCourseDropdown(courses) {
        const courseSelect = document.getElementById('course');
        if (!courseSelect) return;
        
        courseSelect.innerHTML = '<option value="">Select Course</option>';
        
        courses.forEach(course => {
            const option = document.createElement('option');
            option.value = course.courseId;
            option.textContent = `${course.courseCode} - ${course.courseName}`;
            courseSelect.appendChild(option);
        });
    }

    function showNotification(message, type = 'info') {
        // Create a simple notification
        const notification = document.createElement('div');
        notification.className = `notification ${type}`;
        notification.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 15px 20px;
            background: ${type === 'error' ? '#f44336' : type === 'success' ? '#4caf50' : '#2196f3'};
            color: white;
            border-radius: 4px;
            z-index: 10000;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
        `;
        notification.textContent = message;
        
        document.body.appendChild(notification);
        
        setTimeout(() => {
            notification.remove();
        }, 5000);
    }

    // Global functions for onclick handlers
    window.viewAssignment = async function(assignmentId) {
        try {
            const response = await fetch(`/subject-leader/exam-assignments/api/assignments/${assignmentId}`);
            if (response.ok) {
                const assignment = await response.json();
                showAssignmentDetails(assignment);
            }
        } catch (error) {
            console.error('Error loading assignment details:', error);
            showNotification('Error loading assignment details', 'error');
        }
    };

    window.editAssignment = async function(assignmentId) {
        try {
            // Lấy dữ liệu assignment
            const response = await fetch(`/subject-leader/exam-assignments/api/assignments/${assignmentId}`);
            if (response.ok) {
                const assignment = await response.json();
                // Mở modal và điền dữ liệu
                closeAllModals();
                clearNewAssignForm();
                // Gán dữ liệu vào form
                document.getElementById('examTitle').value = assignment.assignmentName || '';
                document.getElementById('lecturer').value = assignment.assignedToId || '';
                // Gọi lại load courses cho lecturer
                await onLecturerChange();
                document.getElementById('course').value = assignment.courseId || '';
                document.getElementById('noOfExams').value = assignment.totalQuestions || '';
                document.getElementById('examType').value = assignment.examType || '';
                document.getElementById('assignDate').value = assignment.createdAt ? formatDate(assignment.createdAt) : '';
                document.getElementById('dueDate').value = assignment.deadline ? formatDate(assignment.deadline) : '';
                document.getElementById('note').value = assignment.note || '';
                // Hiện modal
                if (modalOverlay && newAssignModal) {
                    modalOverlay.style.display = 'block';
                    newAssignModal.style.display = 'block';
                    document.body.classList.add('modal-open');
                }
                // Đổi nút Assign thành Update
                const assignButton = document.querySelector('.assign-button');
                assignButton.textContent = 'Update';
                // Gỡ event cũ, thêm event mới
                const newHandler = async function() {
                    // Lấy dữ liệu từ form
                    const formData = {
                        assignmentName: document.getElementById('examTitle').value,
                        courseId: parseInt(document.getElementById('course').value),
                        assignedToId: parseInt(document.getElementById('lecturer').value),
                        totalQuestions: parseInt(document.getElementById('noOfExams').value) || 20,
                        examType: document.getElementById('examType').value,
                        deadline: document.getElementById('dueDate').value,
                        note: document.getElementById('note').value
                    };
                    // Gửi PUT request cập nhật
                    try {
                        const res = await fetch(`/subject-leader/exam-assignments/api/assignments/${assignmentId}`, {
                            method: 'PUT',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify(formData)
                        });
                        if (res.ok) {
                            showNotification('Assignment updated successfully', 'success');
                            closeAllModals();
                            loadAssignments();
                        } else {
                            const err = await res.json();
                            showNotification(err.error || 'Failed to update assignment', 'error');
                        }
                    } catch (error) {
                        showNotification('Error updating assignment', 'error');
                    }
                };
                assignButton.onclick = newHandler;
            }
        } catch (error) {
            showNotification('Error loading assignment for edit', 'error');
        }
    };

    window.deleteAssignment = async function(assignmentId) {
        if (!confirm('Are you sure you want to delete this assignment?')) {
            return;
        }

        try {
            const response = await fetch(`/subject-leader/exam-assignments/api/assignments/${assignmentId}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                showNotification('Assignment deleted successfully', 'success');
                loadAssignments();
            } else {
                const error = await response.json();
                showNotification(error.error || 'Failed to delete assignment', 'error');
            }
        } catch (error) {
            console.error('Error deleting assignment:', error);
            showNotification('Error deleting assignment', 'error');
        }
    };

    // Define showAssignmentDetails function
    window.showAssignmentDetails = function(assignment) {
        // Close any other open modals first
        closeAllModals();
        
        // Populate details modal
        document.getElementById('detailsExamTitle').value = assignment.assignmentName;
        document.getElementById('detailsLecturer').value = assignment.assignedToName;
        document.getElementById('detailsCourse').value = `${assignment.courseCode} - ${assignment.courseName}`;
        document.getElementById('detailsNoOfExams').value = assignment.totalQuestions || 'Not specified';
        document.getElementById('detailsStructure').value = assignment.instructions || 'Not specified';
        document.getElementById('detailsAssignDate').value = formatDate(assignment.createdAt);
        document.getElementById('detailsDueDate').value = formatDate(assignment.deadline);

        // Show details modal
        const detailsModalOverlay = document.getElementById('detailsModalOverlay');
        const detailsModal = document.getElementById('detailsModal');
        if (detailsModalOverlay && detailsModal) {
            detailsModalOverlay.style.display = 'block';
            detailsModal.style.display = 'block';
            document.body.classList.add('modal-open');
        }
    };

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    initializePage();
});