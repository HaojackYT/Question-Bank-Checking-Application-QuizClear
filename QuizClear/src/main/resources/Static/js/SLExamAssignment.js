// SL Exam Assignment JavaScript
// Handles all frontend interactions for Subject Leader Exam Assignment functionality

document.addEventListener('DOMContentLoaded', () => {
    // Initialize page
    initializePage();
    loadAssignments();
    
    // Modal elements
    const modalOverlay = document.getElementById('newAssignModalOverlay');
    const newAssignModal = document.getElementById('newAssignModal');
    const detailsModal = document.getElementById('detailsModal');
    const detailsModalOverlay = document.getElementById('detailsModalOverlay');
    
    // Buttons
    const newAssignBtn = document.getElementById('newAssignBtn');
    const assignButton = document.querySelector('.assign-button');
    const closeModalButtons = document.querySelectorAll('.close-modal-btn');
    const closeDetailsModalButtons = document.querySelectorAll('.close-details-modal-btn');
    
    // Search and filter
    const searchInput = document.querySelector('.search-input');
    const searchBtn = document.querySelector('.search-btn');
    const statusFilter = document.getElementById('statusFilter');

    // Event listeners
    newAssignBtn?.addEventListener('click', openNewAssignModal);
    assignButton?.addEventListener('click', createAssignment);
    
    closeModalButtons.forEach(btn => btn.addEventListener('click', closeNewAssignModal));
    closeDetailsModalButtons.forEach(btn => btn.addEventListener('click', closeDetailsModal));
    
    // Close modals on overlay click
    modalOverlay?.addEventListener('click', (e) => {
        if (e.target === modalOverlay) closeNewAssignModal();
    });
    
    detailsModalOverlay?.addEventListener('click', (e) => {
        if (e.target === detailsModalOverlay) closeDetailsModal();
    });
    
    // Search functionality
    searchBtn?.addEventListener('click', performSearch);
    searchInput?.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') performSearch();
    });
    
    // Filter functionality
    statusFilter?.addEventListener('change', loadAssignments);

    // Close modals with ESC key
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            closeAllModals();
        }
    });

    // Functions
    function initializePage() {
        console.log('Initializing SL Exam Assignment page...');
        loadDropdownData();
    }

    async function loadDropdownData() {
        try {
            // Load courses
            const coursesResponse = await fetch('/subject-leader/exam-assignments/api/courses');
            if (coursesResponse.ok) {
                const courses = await coursesResponse.json();
                populateCourseDropdown(courses);
            }

            // Load lecturers  
            const lecturersResponse = await fetch('/subject-leader/exam-assignments/api/lecturers');
            if (lecturersResponse.ok) {
                const lecturers = await lecturersResponse.json();
                populateLecturerDropdown(lecturers);
            }
        } catch (error) {
            console.error('Error loading dropdown data:', error);
            showNotification('Error loading form data', 'error');
        }
    }

    function populateCourseDropdown(courses) {
        const courseSelect = document.getElementById('course');
        if (courseSelect) {
            courseSelect.innerHTML = '<option value="">Select Course</option>';
            courses.forEach(course => {
                const option = document.createElement('option');
                option.value = course.courseId;
                option.textContent = `${course.courseCode} - ${course.courseName}`;
                courseSelect.appendChild(option);
            });
        }
    }

    function populateLecturerDropdown(lecturers) {
        const lecturerSelect = document.getElementById('lecturer');
        if (lecturerSelect) {
            lecturerSelect.innerHTML = '<option value="">Select Lecturer</option>';
            lecturers.forEach(lecturer => {
                const option = document.createElement('option');
                option.value = lecturer.userId;
                option.textContent = lecturer.fullName;
                lecturerSelect.appendChild(option);
            });
        }
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

        tbody.innerHTML = assignments.map((assignment, index) => `
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
                    ${assignment.canEdit ? `
                        <button class="action-icon edit-icon" onclick="editAssignment(${assignment.assignmentId})">
                            <i class="fa-solid fa-pen-to-square"></i>
                        </button>
                        <button class="action-icon delete-icon" onclick="deleteAssignment(${assignment.assignmentId})">
                            <i class="fa-solid fa-trash"></i>
                        </button>
                    ` : ''}
                </td>
            </tr>
        `).join('');
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
        document.getElementById('structure').value = '';
        document.getElementById('assignDate').value = '';
        document.getElementById('dueDate').value = '';
    }

    async function createAssignment() {
        try {
            const formData = {
                assignmentName: document.getElementById('examTitle').value,
                description: '', // You might want to add a description field
                courseId: parseInt(document.getElementById('course').value),
                assignedToId: parseInt(document.getElementById('lecturer').value),
                deadline: parseDateInput(document.getElementById('dueDate').value),
                totalQuestions: parseInt(document.getElementById('noOfExams').value),
                durationMinutes: 90, // Default or add field for this
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
                closeNewAssignModal();
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
        // Assuming format DD/MM/YY or DD/MM/YYYY
        const parts = dateString.split('/');
        if (parts.length === 3) {
            let [day, month, year] = parts;
            if (year.length === 2) {
                year = '20' + year;
            }
            return new Date(year, month - 1, day).toISOString();
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

    window.editAssignment = function(assignmentId) {
        // TODO: Implement edit functionality
        console.log('Edit assignment:', assignmentId);
        showNotification('Edit functionality coming soon', 'info');
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
    };    function showAssignmentDetails(assignment) {
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
        detailsModalOverlay.style.display = 'block';
        detailsModal.style.display = 'block';
        document.body.classList.add('modal-open');
    }
});