// slTaskManagement.js

let allTasks = [];
let lecturers = [];

// Load tasks when page loads
document.addEventListener('DOMContentLoaded', function() {
    loadTasks();
    loadLecturers();
    initializeEventListeners();
});

// Initialize event listeners
function initializeEventListeners() {
    // Delegate form submission
    document.getElementById('delegateForm').addEventListener('submit', handleDelegateSubmit);
    
    // Close modals when clicking outside
    window.addEventListener('click', function(event) {
        if (event.target.classList.contains('modal')) {
            event.target.style.display = 'none';
        }
    });
}

// Load tasks from API
async function loadTasks() {
    try {
        showLoading();
        const response = await fetch('/subject-leader/api/task-management/tasks');
        
        if (!response.ok) {
            throw new Error('Failed to load tasks');
        }
        
        const tasks = await response.json();
        allTasks = tasks;
        displayTasks(tasks);
    } catch (error) {
        console.error('Error loading tasks:', error);
        showMessage('Error loading tasks: ' + error.message, 'error');
        showErrorInTable();
    }
}

// Load lecturers for delegation
async function loadLecturers() {
    try {
        const response = await fetch('/subject-leader/api/task-management/lecturers');
        
        if (!response.ok) {
            throw new Error('Failed to load lecturers');
        }
        
        lecturers = await response.json();
        populateLecturerSelect();
    } catch (error) {
        console.error('Error loading lecturers:', error);
    }
}

// Display tasks in table
function displayTasks(tasks) {
    const tbody = document.getElementById('taskTableBody');
    
    if (!tasks || tasks.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="8" class="loading-cell">
                    <i class="fas fa-inbox"></i> No tasks assigned yet
                </td>
            </tr>
        `;
        return;
    }
    
    tbody.innerHTML = tasks.map(task => {
        const statusClass = getStatusClass(task.status);
        const actionButtons = getActionButtons(task);
        
        return `
            <tr>
                <td>${task.taskId}</td>
                <td>${task.title}</td>
                <td>${task.courseName || 'N/A'}</td>
                <td>${task.totalQuestions}</td>
                <td>${task.assignedByName}</td>
                <td>${formatDate(task.dueDate)}</td>
                <td><span class="status-badge ${statusClass}">${formatStatus(task.status)}</span></td>
                <td>
                    <div class="action-buttons">
                        ${actionButtons}
                    </div>
                </td>
            </tr>
        `;
    }).join('');
}

// Get status CSS class
function getStatusClass(status) {
    const statusMap = {
        'pending': 'status-pending',
        'in_progress': 'status-in_progress', 
        'completed': 'status-completed',
        'cancelled': 'status-cancelled'
    };
    return statusMap[status] || 'status-pending';
}

// Format status for display
function formatStatus(status) {
    const statusMap = {
        'pending': 'Pending',
        'in_progress': 'In Progress',
        'completed': 'Completed',
        'cancelled': 'Cancelled'
    };
    return statusMap[status] || status;
}

// Get action buttons based on task status
function getActionButtons(task) {
    let buttons = `<button class="action-btn view-btn" onclick="viewTaskDetails(${task.taskId})">
                       <i class="fas fa-eye"></i> View
                   </button>`;
    
    if (task.status === 'pending') {
        buttons += `<button class="action-btn accept-btn" onclick="acceptTask(${task.taskId})">
                        <i class="fas fa-check"></i> Accept
                    </button>`;
    }
    
    if (task.status === 'in_progress') {
        buttons += `<button class="action-btn complete-btn" onclick="completeTask(${task.taskId})">
                        <i class="fas fa-check-circle"></i> Complete
                    </button>
                    <button class="action-btn delegate-btn" onclick="openDelegateModal(${task.taskId})">
                        <i class="fas fa-user-plus"></i> Delegate
                    </button>`;
    }    if (task.status === 'completed') {
        // For completed tasks, only show view button (already added above)
        buttons += `<span class="status-info">
                       <i class="fas fa-check-circle"></i> Completed
                   </span>`;
    }
    
    if (task.status === 'cancelled') {
        // Check if this is a delegated task
        if (task.description && task.description.includes('[DELEGATED]')) {
            buttons += `<span class="status-info">
                           <i class="fas fa-user-check"></i> Delegated
                       </span>`;
        } else {
            buttons += `<span class="status-info">
                           <i class="fas fa-times-circle"></i> Cancelled
                       </span>`;
        }
    }
    
    return buttons;
}

// Format date for display
function formatDate(dateString) {
    if (!dateString) return 'N/A';
    
    const date = new Date(dateString);
    return date.toLocaleDateString('en-GB', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
}

// View task details
function viewTaskDetails(taskId) {
    const task = allTasks.find(t => t.taskId === taskId);
    if (!task) return;
    
    document.getElementById('detailTitle').value = task.title;
    document.getElementById('detailCourse').value = task.courseName || 'N/A';
    document.getElementById('detailTotalQuestions').value = task.totalQuestions;
    document.getElementById('detailAssignedBy').value = task.assignedByName;
    document.getElementById('detailDueDate').value = formatDate(task.dueDate);
    document.getElementById('detailStatus').value = formatStatus(task.status);
    document.getElementById('detailDescription').value = task.description || '';
    
    openModal('taskDetailsModal');
}

// Accept task
async function acceptTask(taskId) {
    if (!confirm('Are you sure you want to accept this task?')) return;
    
    try {
        const response = await fetch(`/subject-leader/api/task-management/tasks/${taskId}/accept`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        if (!response.ok) {
            throw new Error('Failed to accept task');
        }
        
        const result = await response.json();
        showMessage('Task accepted successfully!', 'success');
        loadTasks(); // Reload tasks
    } catch (error) {
        console.error('Error accepting task:', error);
        showMessage('Error accepting task: ' + error.message, 'error');
    }
}

// Complete task
async function completeTask(taskId) {
    if (!confirm('Are you sure you want to mark this task as completed?')) return;
    
    try {
        const response = await fetch(`/subject-leader/api/task-management/tasks/${taskId}/complete`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        if (!response.ok) {
            throw new Error('Failed to complete task');
        }
        
        const result = await response.json();
        showMessage('Task completed successfully!', 'success');
        loadTasks(); // Reload tasks
    } catch (error) {
        console.error('Error completing task:', error);
        showMessage('Error completing task: ' + error.message, 'error');
    }
}

// Open delegate modal
function openDelegateModal(taskId) {
    const task = allTasks.find(t => t.taskId === taskId);
    if (!task) return;
    
    document.getElementById('delegateTaskId').value = taskId;
    document.getElementById('delegateTaskTitle').value = task.title;
    document.getElementById('delegateNotes').value = '';
    document.getElementById('lecturerSelect').value = '';
    
    openModal('delegateModal');
}

// Handle delegate form submission
async function handleDelegateSubmit(event) {
    event.preventDefault();
    
    const taskId = document.getElementById('delegateTaskId').value;
    const lecturerId = document.getElementById('lecturerSelect').value;
    const notes = document.getElementById('delegateNotes').value;
    
    if (!lecturerId) {
        showMessage('Please select a lecturer', 'error');
        return;
    }
    
    // Show loading state
    const submitBtn = event.target.querySelector('button[type="submit"]');
    const originalText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Delegating...';
    submitBtn.disabled = true;
    
    try {
        const response = await fetch(`/subject-leader/api/task-management/tasks/${taskId}/delegate`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                lecturerId: parseInt(lecturerId),
                notes: notes
            })
        });
        
        if (!response.ok) {
            throw new Error('Failed to delegate task');
        }
        
        const result = await response.json();
        showMessage('Task delegated successfully! The task status has been updated.', 'success');
        closeModal('delegateModal');
        
        // Add small delay then reload to ensure backend has processed
        setTimeout(() => {
            loadTasks(); // Reload tasks to refresh status and buttons
        }, 500);
        
    } catch (error) {
        console.error('Error delegating task:', error);
        showMessage('Error delegating task: ' + error.message, 'error');
    } finally {
        // Restore button state
        submitBtn.innerHTML = originalText;
        submitBtn.disabled = false;
    }
}

// Populate lecturer select dropdown
function populateLecturerSelect() {
    const select = document.getElementById('lecturerSelect');
    
    // Clear existing options (keep the first one)
    select.innerHTML = '<option value="">Choose a lecturer...</option>';
    
    lecturers.forEach(lecturer => {
        const option = document.createElement('option');
        option.value = lecturer.userId;
        option.textContent = `${lecturer.fullName} (${lecturer.department})`;
        select.appendChild(option);
    });
}

// Filter tasks
function filterTasks() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const statusFilter = document.getElementById('statusFilter').value;
    
    let filteredTasks = allTasks;
    
    // Apply search filter
    if (searchTerm) {
        filteredTasks = filteredTasks.filter(task => 
            task.title.toLowerCase().includes(searchTerm) ||
            task.courseName?.toLowerCase().includes(searchTerm) ||
            task.assignedByName.toLowerCase().includes(searchTerm)
        );
    }
    
    // Apply status filter
    if (statusFilter) {
        filteredTasks = filteredTasks.filter(task => task.status === statusFilter);
    }
    
    displayTasks(filteredTasks);
}

// Modal functions
function openModal(modalId) {
    document.getElementById(modalId).style.display = 'block';
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

// Show loading state
function showLoading() {
    const tbody = document.getElementById('taskTableBody');
    tbody.innerHTML = `
        <tr>
            <td colspan="8" class="loading-cell">
                <i class="fas fa-spinner fa-spin"></i> Loading tasks...
            </td>
        </tr>
    `;
}

// Show error in table
function showErrorInTable() {
    const tbody = document.getElementById('taskTableBody');
    tbody.innerHTML = `
        <tr>
            <td colspan="8" class="loading-cell">
                <i class="fas fa-exclamation-triangle"></i> Error loading tasks. Please try again.
            </td>
        </tr>
    `;
}

// Show message toast
function showMessage(message, type = 'success') {
    const toast = document.getElementById('messageToast');
    const messageText = document.getElementById('messageText');
    
    messageText.textContent = message;
    toast.className = `toast ${type}`;
    toast.classList.add('show');
    
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}
