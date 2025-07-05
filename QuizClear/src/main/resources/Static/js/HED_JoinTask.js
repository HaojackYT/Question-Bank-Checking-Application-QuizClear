// HED Join Task JavaScript - Updated to use real data
document.addEventListener('DOMContentLoaded', function() {
    initializeTaskPage();
});

let allTasksData = [];
let filteredTasksData = [];

// Initialize page
async function initializeTaskPage() {
    try {
        console.log('Initializing HED Join Task page...');
        
        // Load task data from API
        await loadTaskData();
        
        // Load notifications
        await loadTaskNotifications();
        
        // Setup event listeners
        setupEventListeners();
        
        // Setup filters
        setupFilters();
        
    } catch (error) {
        console.error('Error initializing page:', error);
        showErrorMessage('Failed to load page data');
    }
}

// Load task data from backend API
async function loadTaskData() {
    try {
        const response = await fetch('/api/hed/tasks', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        allTasksData = data || [];
        filteredTasksData = [...allTasksData];
        
        console.log('Loaded task data:', allTasksData);
        
        // Update table if we have data
        if (allTasksData.length > 0) {
            updateTaskTable(allTasksData);
        }
        
    } catch (error) {
        console.error('Error loading task data:', error);
        // Keep the Thymeleaf-rendered data as fallback
        console.log('Using server-side rendered data as fallback');
    }
}

// Load task notifications from API
async function loadTaskNotifications() {
    try {
        const response = await fetch('/api/hed/task-notifications', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const notifications = await response.json();
        updateTaskNotifications(notifications);
        
    } catch (error) {
        console.error('Error loading task notifications:', error);
        // Keep server-side rendered notifications
    }
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

// Update task table with data
function updateTaskTable(tasks) {
    const tableBody = document.getElementById('taskTableBody');
    if (!tableBody) return;
    
    if (!tasks || tasks.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="7" style="text-align: center; padding: 2rem; color: #666;">
                    No tasks assigned to your department at this time
                </td>
            </tr>
        `;
        return;
    }
    
    const rows = tasks.map(task => `
        <tr data-task-id="${task.id}">
            <td>${task.id}</td>
            <td>${task.subjectName || 'N/A'}</td>
            <td>${formatDate(task.deadline)}</td>
            <td>${task.totalQuestions || 0}</td>
            <td>${task.assignedStaff || 'N/A'}</td>
            <td>
                <span class="status-badge ${task.status?.toLowerCase() || 'pending'}">${task.status || 'Pending'}</span>
            </td>
            <td>
                <button class="action-btn view-btn" title="View" onclick="viewTask(${task.id})">
                    <i class="fas fa-eye"></i>
                </button>
                ${(task.status === 'ASSIGNED' || task.status === 'PENDING') ? `
                    <button class="action-btn edit-btn" title="Join Task" onclick="joinTask(${task.id})">
                        <i class="fas fa-user-plus"></i>
                    </button>
                ` : ''}
                ${task.status === 'COMPLETED' ? `
                    <button class="action-btn delete-btn" title="Remove" onclick="removeTask(${task.id})">
                        <i class="fas fa-trash"></i>
                    </button>
                ` : ''}
            </td>
        </tr>
    `).join('');
    
    tableBody.innerHTML = rows;
}

// Update task notifications
function updateTaskNotifications(notifications) {
    const container = document.getElementById('taskNotificationContainer');
    if (!container) return;
    
    if (!notifications || notifications.length === 0) {
        container.innerHTML = `
            <div class="notification_item">
                <div class="notification-text">
                    <strong>No new task assignments</strong>
                    <p>All tasks are up to date</p>
                </div>
            </div>
        `;
        return;
    }
    
    const notificationHtml = notifications.map(notif => `
        <div class="notification_item">
            <input type="checkbox" checked>
            <div class="notification-text">
                <strong>${notif.title || 'New task assignment'}</strong>
                <p>Deadline: ${formatDate(notif.deadline)}</p>
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
        searchInput.addEventListener('input', handleTaskSearch);
    }
    
    // Filter dropdowns
    const statusFilter = document.querySelector('.filter-select');
    const subjectFilter = document.querySelectorAll('.filter-select')[1];
    
    if (statusFilter) {
        statusFilter.addEventListener('change', applyTaskFilters);
    }
    if (subjectFilter) {
        subjectFilter.addEventListener('change', applyTaskFilters);
    }
    
    // Search button
    const searchBtn = document.querySelector('.search-btn');
    if (searchBtn) {
        searchBtn.addEventListener('click', handleTaskSearch);
    }
}

// Setup filters with dynamic data
function setupFilters() {
    if (allTasksData.length === 0) return;
    
    // Get unique subjects for subject filter
    const subjects = [...new Set(allTasksData.map(task => task.subjectName).filter(Boolean))];
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
function handleTaskSearch() {
    const searchTerm = document.querySelector('.search-input')?.value?.toLowerCase() || '';
    
    let filtered = [...allTasksData];
    
    if (searchTerm) {
        filtered = filtered.filter(task => 
            task.subjectName?.toLowerCase().includes(searchTerm) ||
            task.assignedStaff?.toLowerCase().includes(searchTerm) ||
            task.id?.toString().includes(searchTerm)
        );
    }
    
    filteredTasksData = filtered;
    applyTaskFilters();
}

// Apply filters
function applyTaskFilters() {
    const statusFilter = document.querySelector('.filter-select');
    const subjectFilter = document.querySelectorAll('.filter-select')[1];
    
    const statusValue = statusFilter?.value || '';
    const subjectValue = subjectFilter?.value || '';
    
    let filtered = [...filteredTasksData];
    
    if (statusValue) {
        filtered = filtered.filter(task => 
            task.status?.toLowerCase() === statusValue.toLowerCase()
        );
    }
    
    if (subjectValue) {
        filtered = filtered.filter(task => 
            task.subjectName === subjectValue
        );
    }
    
    updateTaskTable(filtered);
}

// Action functions
function viewTask(taskId) {
    console.log('Viewing task:', taskId);
    window.location.href = `/api/hed/tasks/${taskId}/details`;
}

function joinTask(taskId) {
    if (confirm('Are you sure you want to join this task?')) {
        updateTaskStatus(taskId, 'JOINED');
    }
}

function removeTask(taskId) {
    if (confirm('Are you sure you want to remove this task?')) {
        updateTaskStatus(taskId, 'REMOVED');
    }
}

// Update task status
async function updateTaskStatus(taskId, action) {
    try {
        const response = await fetch(`/api/hed/tasks/${taskId}/${action.toLowerCase()}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        if (response.ok) {
            alert(`Task ${action.toLowerCase()} successfully!`);
            // Reload data
            await loadTaskData();
            await loadTaskNotifications();
        } else {
            throw new Error(`Failed to ${action.toLowerCase()} task`);
        }
    } catch (error) {
        console.error(`Error ${action.toLowerCase()} task:`, error);
        alert(`Failed to ${action.toLowerCase()} task. Please try again.`);
    }
}

// Load more task notifications
function loadMoreTaskNotifications() {
    console.log('Loading more task notifications...');
    // Implement pagination if needed
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

// Show error message
function showErrorMessage(message) {
    console.error(message);
    const tableBody = document.getElementById('taskTableBody');
    if (tableBody) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="7" style="text-align: center; padding: 2rem; color: #dc3545;">
                    <i class="fas fa-exclamation-triangle"></i> ${message}
                </td>
            </tr>
        `;
    }
}

// Close modal when clicking outside
window.addEventListener('click', function(event) {
    const modal = document.getElementById('courseDetailsModal');
    if (event.target === modal) {
        closeTaskModal();
    }
});
