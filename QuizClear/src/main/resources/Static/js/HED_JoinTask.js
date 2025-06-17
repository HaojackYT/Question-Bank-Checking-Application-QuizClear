// HED Join Task JavaScript
document.addEventListener('DOMContentLoaded', function() {
    // Initialize page
    loadTaskData();
    setupEventListeners();
});

// Load task data from backend
async function loadTaskData() {    try {
        const response = await fetch('/api/hed/tasks');
        if (response.ok) {
            const tasks = await response.json();
            populateTaskTable(tasks);
        } else {
            console.error('Failed to load task data');
            // Show error message instead of mock data
            showErrorMessage('Failed to load task data from server');
        }
    } catch (error) {
        console.error('Error loading task data:', error);
        showErrorMessage('Error connecting to server: ' + error.message);
    }
}

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

// Populate the task table with data
function populateTaskTable(tasks) {
    const tbody = document.querySelector('.exam-table tbody');
    if (!tbody) return;

    tbody.innerHTML = '';

    tasks.forEach(task => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${task.taskId}</td>
            <td>${task.subjectName}</td>
            <td>${formatDate(task.deadline)}</td>
            <td>${task.totalQuestions}</td>
            <td>${task.assignedLecturerName}</td>
            <td><span class="status-badge ${getStatusClass(task.status)}">${task.status}</span></td>
            <td>
                <div class="action-buttons">
                    <button class="action-btn" title="View" onclick="viewTaskDetails('${task.taskId}')">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="action-btn" title="Edit" onclick="editTask('${task.taskId}')">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="action-btn" title="Delete" onclick="deleteTask('${task.taskId}')">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        `;
        tbody.appendChild(row);
    });

    // Setup event listeners for action buttons
    setupActionButtons();
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
        case 'assigned': return 'approved';
        case 'received': return 'in-progress';
        case 'pending': return 'pending';
        case 'incomplete': return 'rejected';
        case 'in_progress': return 'in-progress';
        case 'completed': return 'approved';
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

// Setup action buttons
function setupActionButtons() {
    // Remove existing event listeners and add new ones
    const actionButtons = document.querySelectorAll('.action-btn');
    actionButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            const icon = this.querySelector('i');
            const row = this.closest('tr');
            const taskId = row.querySelector('td').textContent;
            
            if (icon.classList.contains('fa-eye')) {
                viewTaskDetails(taskId);
            } else if (icon.classList.contains('fa-edit')) {
                editTask(taskId);
            } else if (icon.classList.contains('fa-trash')) {
                deleteTask(taskId);
            }
        });
    });
}

// Perform search and filter
async function performSearch() {
    const searchQuery = document.querySelector('.search-input')?.value || '';
    const statusFilter = document.querySelector('.filter-select')?.value || '';
    const subjectFilter = document.querySelector('.filter-select')?.parentElement?.nextElementSibling?.querySelector('select')?.value || '';

    try {
        const response = await fetch(`/api/hed/tasks/search?query=${encodeURIComponent(searchQuery)}&status=${encodeURIComponent(statusFilter)}&subject=${encodeURIComponent(subjectFilter)}`);
        if (response.ok) {
            const tasks = await response.json();
            populateTaskTable(tasks);
        }
    } catch (error) {
        console.error('Error searching tasks:', error);
    }
}

// View task details
function viewTaskDetails(taskId) {
    const modal = document.getElementById('courseDetailsModal');
    if (!modal) {
        console.error('Course modal not found');
        return;
    }

    // Find task data
    const row = document.querySelector(`td:first-child`);
    const taskData = {
        taskId: taskId,
        title: "Course for advanced students",
        subjectName: "Programming",
        totalQuestions: 45,
        assignedLecturerName: "Nguyen Van A",
        deadline: "2025-08-15",
        status: "Active"
    };

    openTaskModal(taskData);
}

// Open task details modal
function openTaskModal(task) {
    const modal = document.getElementById('courseDetailsModal');
    if (!modal) return;

    // Populate modal with task data
    document.getElementById('modalStaff').textContent = task.assignedLecturerName || 'N/A';
    document.getElementById('modalTitle').textContent = task.title + " course for advanced students";
    document.getElementById('modalTotalQuestion').textContent = task.totalQuestions || 'N/A';
    document.getElementById('modalSubject').textContent = task.subjectName || 'N/A';
    document.getElementById('modalDeadline').textContent = formatDate(task.deadline);
    document.getElementById('modalDueDate').textContent = formatDate(task.deadline);
    
    const statusElement = document.getElementById('modalStatus');
    if (statusElement) {
        statusElement.textContent = task.status || 'Active';
        statusElement.className = getStatusClass(task.status || 'Active');
    }

    document.getElementById('modalDescription').textContent = 
        `This is a comprehensive ${(task.subjectName || 'programming').toLowerCase()} course that covers modern technologies and best practices for students.`;

    modal.style.display = 'block';

    // Setup modal buttons
    setupTaskModalButtons(task.taskId);
}

// Setup task modal buttons
function setupTaskModalButtons(taskId) {
    const approveBtn = document.querySelector('#courseDetailsModal .approve-btn');
    const rejectBtn = document.querySelector('#courseDetailsModal .reject-btn');
    const closeBtn = document.querySelector('#courseDetailsModal .close-btn');
    const expandBtn = document.querySelector('#courseDetailsModal .expand-btn');

    if (approveBtn) {
        approveBtn.onclick = () => approveTask(taskId);
    }
    
    if (rejectBtn) {
        rejectBtn.onclick = () => rejectTask(taskId);
    }

    if (closeBtn) {
        closeBtn.onclick = () => closeTaskModal();
    }

    if (expandBtn) {
        expandBtn.onclick = () => toggleExpand();
    }
}

// Approve task
function approveTask(taskId) {
    alert('Task approved successfully!');
    closeTaskModal();
    loadTaskData(); // Refresh the table
}

// Reject task
function rejectTask(taskId) {
    const feedback = document.querySelector('#courseDetailsModal #modalFeedback')?.value || '';
    
    if (!feedback.trim()) {
        alert('Please provide feedback before rejecting the task.');
        return;
    }

    alert('Task rejected with feedback: ' + feedback);
    closeTaskModal();
    loadTaskData(); // Refresh the table
}

// Close task modal
function closeTaskModal() {
    const modal = document.getElementById('courseDetailsModal');
    if (modal) {
        modal.style.display = 'none';
    }
}

// Toggle expand modal
function toggleExpand() {
    const modal = document.querySelector('#courseDetailsModal .details-modal');
    if (modal) {
        modal.classList.toggle('expanded');
    }
}

// Edit task
function editTask(taskId) {
    alert(`Edit functionality for task ${taskId} - To be implemented`);
}

// Delete task
function deleteTask(taskId) {
    if (confirm(`Are you sure you want to delete task "${taskId}"?`)) {
        alert(`Task "${taskId}" has been deleted!`);
        loadTaskData(); // Refresh the table
    }
}

// Close modal when clicking outside
window.addEventListener('click', function(event) {
    const modal = document.getElementById('courseDetailsModal');
    if (event.target === modal) {
        closeTaskModal();
    }
});
