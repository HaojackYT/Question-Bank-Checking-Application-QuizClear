// Lecturer Dashboard JavaScript with scope filtering
document.addEventListener('DOMContentLoaded', function() {
    initializeLecturerDashboard();
});

// Global scope variables for lecturer dashboard
let lecturerScope = {
    userId: null,
    userRole: 'LECTURER',
    assignedDepartmentIds: [],
    assignedSubjectIds: [],
    managedQuestions: [],
    assignedTasks: []
};

// Initialize lecturer dashboard with scope awareness
async function initializeLecturerDashboard() {
    try {
        console.log('=== Initializing lecturer dashboard with scope ===');
        
        // Load lecturer scope first
        await loadLecturerScope();
        
        // Load dashboard data with scope filtering
        await loadDashboardDataWithScope();
        
    } catch (error) {
        console.error('Error initializing lecturer dashboard:', error);
        loadFallbackDashboard();
    }
}

// Load lecturer's scope (assigned departments and subjects)
async function loadLecturerScope() {
    try {
        const response = await fetch('/api/user/current-scope', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            lecturerScope = await response.json();
            console.log('Lecturer scope loaded:', lecturerScope);
            
            // Update scope indicator
            updateScopeIndicator();
        } else {
            console.error('Failed to load lecturer scope');
        }
    } catch (error) {
        console.error('Error loading lecturer scope:', error);
    }
}

// Load dashboard data with scope filtering
async function loadDashboardDataWithScope() {
    console.log('=== Loading dashboard data with scope filtering ===');
    
    await Promise.all([
        loadStatsWithScope(),
        loadTasksWithScope(),
        loadActivitiesWithScope()
    ]);
}

// Load stats filtered by lecturer scope
async function loadStatsWithScope() {
    console.log('=== Loading stats with scope filtering ===');
    
    try {
        const params = new URLSearchParams();
        if (lecturerScope.assignedDepartmentIds.length > 0) {
            params.append('departmentIds', lecturerScope.assignedDepartmentIds.join(','));
        }
        if (lecturerScope.assignedSubjectIds.length > 0) {
            params.append('subjectIds', lecturerScope.assignedSubjectIds.join(','));
        }
        params.append('requestingUserId', lecturerScope.userId);
        params.append('userRole', lecturerScope.userRole);

        const response = await fetch(`/api/dashboard/lecturer/stats?${params}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const data = await response.json();
            console.log('*** Scope-filtered stats received:', data);
            updateStatsDisplay(data);
        } else {
            throw new Error('Failed to fetch scope-filtered stats: ' + response.status);
        }
    } catch (error) {
        console.error('Error loading scope-filtered stats:', error);
        
        // Fallback to test endpoint
        try {
            const fallbackResponse = await fetch('/api/dashboard/lecturer/test-stats');
            const fallbackData = await fallbackResponse.json();
            console.log('*** Using fallback stats data:', fallbackData);
            updateStatsDisplay(fallbackData);
        } catch (fallbackError) {
            console.error('Fallback stats also failed:', fallbackError);
            updateStatsDisplay({
                questionSubmitted: 0,
                questionApproved: 0,
                questionReturned: 0
            });
        }
    }
}

// Load tasks filtered by lecturer scope
async function loadTasksWithScope() {
    console.log('=== Loading tasks with scope filtering ===');
    
    try {
        const params = new URLSearchParams();
        if (lecturerScope.assignedSubjectIds.length > 0) {
            params.append('subjectIds', lecturerScope.assignedSubjectIds.join(','));
        }
        params.append('assigneeId', lecturerScope.userId);
        params.append('userRole', lecturerScope.userRole);

        const response = await fetch(`/api/dashboard/lecturer/tasks?${params}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const data = await response.json();
            console.log('*** Scope-filtered tasks received:', data);
            updateTasksDisplay(data);
        } else {
            throw new Error('Failed to fetch scope-filtered tasks: ' + response.status);
        }
    } catch (error) {
        console.error('Error loading scope-filtered tasks:', error);
        
        // Fallback to test endpoint
        try {
            const fallbackResponse = await fetch('/api/dashboard/lecturer/test-tasks');
            const fallbackData = await fallbackResponse.json();
            console.log('*** Using fallback tasks data:', fallbackData);
            updateTasksDisplay(fallbackData);
        } catch (fallbackError) {
            console.error('Fallback tasks also failed:', fallbackError);
            updateTasksDisplay([]);
        }
    }
}

// Load activities filtered by lecturer scope
async function loadActivitiesWithScope() {
    console.log('=== Loading activities with scope filtering ===');
    
    try {
        const params = new URLSearchParams();
        if (lecturerScope.assignedDepartmentIds.length > 0) {
            params.append('departmentIds', lecturerScope.assignedDepartmentIds.join(','));
        }
        if (lecturerScope.assignedSubjectIds.length > 0) {
            params.append('subjectIds', lecturerScope.assignedSubjectIds.join(','));
        }
        params.append('userId', lecturerScope.userId);

        const response = await fetch(`/api/dashboard/lecturer/activities?${params}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const data = await response.json();
            console.log('*** Scope-filtered activities received:', data);
            updateActivitiesDisplay(data);
        } else {
            throw new Error('Failed to fetch scope-filtered activities: ' + response.status);
        }
    } catch (error) {
        console.error('Error loading scope-filtered activities:', error);
        updateActivitiesDisplay([]);
    }
}

// Update scope indicator to show lecturer's assigned subjects
function updateScopeIndicator() {
    let scopeIndicator = document.getElementById('lecturer-scope-indicator');
    if (!scopeIndicator) {
        scopeIndicator = document.createElement('div');
        scopeIndicator.id = 'lecturer-scope-indicator';
        scopeIndicator.className = 'scope-indicator-lecturer';
        
        const header = document.querySelector('.dashboard-header') || document.querySelector('h1');
        if (header) {
            header.parentNode.insertBefore(scopeIndicator, header.nextSibling);
        }
    }
    
    const deptCount = lecturerScope.assignedDepartmentIds.length;
    const subjCount = lecturerScope.assignedSubjectIds.length;
    
    if (subjCount > 0) {
        scopeIndicator.innerHTML = `
            <div class="scope-info">
                <span class="scope-label">Your Assignments:</span>
                <span class="scope-details">${subjCount} subjects across ${deptCount} departments</span>
            </div>
        `;
        scopeIndicator.className = 'scope-indicator-lecturer assigned';
    } else {
        scopeIndicator.innerHTML = `
            <div class="scope-info">
                <span class="scope-label">No Subject Assignments</span>
                <span class="scope-details">Contact your department head for subject assignments</span>
            </div>
        `;
        scopeIndicator.className = 'scope-indicator-lecturer unassigned';
    }
}

// Enhanced stats display with scope awareness
function updateStatsDisplay(data) {
    // Update question stats
    const questionSubmitted = document.getElementById('questions-submitted');
    const questionApproved = document.getElementById('questions-approved');
    const questionReturned = document.getElementById('questions-returned');
    
    if (questionSubmitted) {
        questionSubmitted.textContent = data.questionSubmitted || 0;
    }
    if (questionApproved) {
        questionApproved.textContent = data.questionApproved || 0;
    }
    if (questionReturned) {
        questionReturned.textContent = data.questionReturned || 0;
    }
    
    // Add scope context to stats
    addScopeContextToStats(data);
    
    console.log('Stats display updated with scope awareness');
}

// Enhanced tasks display with scope filtering
function updateTasksDisplay(tasks) {
    const tasksContainer = document.getElementById('tasks-list') || document.querySelector('.tasks-container');
    if (!tasksContainer) {
        console.error('Tasks container not found');
        return;
    }
    
    tasksContainer.innerHTML = '';
    
    // Filter tasks by scope
    const filteredTasks = tasks.filter(task => isTaskInLecturerScope(task));
    
    if (filteredTasks.length === 0) {
        tasksContainer.innerHTML = `
            <div class="no-tasks-message">
                <p>No tasks assigned for your subjects</p>
                <small>Contact your subject leader if you expect assignments</small>
            </div>
        `;
        return;
    }
    
    filteredTasks.forEach(task => {
        const taskElement = createTaskElement(task);
        tasksContainer.appendChild(taskElement);
    });
    
    console.log(`Displayed ${filteredTasks.length} tasks within lecturer scope`);
}

// Enhanced activities display with scope filtering
function updateActivitiesDisplay(activities) {
    const activitiesContainer = document.getElementById('activities-list') || document.querySelector('.activities-container');
    if (!activitiesContainer) {
        console.error('Activities container not found');
        return;
    }
    
    activitiesContainer.innerHTML = '';
    
    // Filter activities by scope
    const filteredActivities = activities.filter(activity => isActivityInLecturerScope(activity));
    
    if (filteredActivities.length === 0) {
        activitiesContainer.innerHTML = `
            <div class="no-activities-message">
                <p>No recent activities in your assigned subjects</p>
            </div>
        `;
        return;
    }
    
    filteredActivities.slice(0, 10).forEach(activity => {
        const activityElement = createActivityElement(activity);
        activitiesContainer.appendChild(activityElement);
    });
    
    console.log(`Displayed ${filteredActivities.length} activities within lecturer scope`);
}

// Check if task is within lecturer's scope
function isTaskInLecturerScope(task) {
    // Task must be assigned to this lecturer
    if (task.assigneeId !== lecturerScope.userId) {
        return false;
    }
    
    // Task subject must be in lecturer's assigned subjects
    if (lecturerScope.assignedSubjectIds.length > 0 && task.subjectId) {
        return lecturerScope.assignedSubjectIds.includes(task.subjectId);
    }
    
    return true;
}

// Check if activity is within lecturer's scope
function isActivityInLecturerScope(activity) {
    // Activity must be related to lecturer's subjects or created by lecturer
    if (activity.userId === lecturerScope.userId) {
        return true;
    }
    
    // Activity subject must be in lecturer's assigned subjects
    if (lecturerScope.assignedSubjectIds.length > 0 && activity.subjectId) {
        return lecturerScope.assignedSubjectIds.includes(activity.subjectId);
    }
    
    return false;
}

// Add scope context information to stats
function addScopeContextToStats(data) {
    const statsContainer = document.querySelector('.stats-container') || document.querySelector('.dashboard-stats');
    if (!statsContainer) return;
    
    let contextElement = document.getElementById('stats-scope-context');
    if (!contextElement) {
        contextElement = document.createElement('div');
        contextElement.id = 'stats-scope-context';
        contextElement.className = 'stats-scope-context';
        statsContainer.appendChild(contextElement);
    }
    
    const subjCount = lecturerScope.assignedSubjectIds.length;
    if (subjCount > 0) {
        contextElement.innerHTML = `
            <small class="scope-note">
                Statistics for your ${subjCount} assigned subject${subjCount > 1 ? 's' : ''}
            </small>
        `;
    } else {
        contextElement.innerHTML = `
            <small class="scope-note warning">
                No subjects assigned - contact your department for assignments
            </small>
        `;
    }
}

// Fallback dashboard when API fails
function loadFallbackDashboard() {
    console.log('Loading fallback dashboard data');
    
    updateStatsDisplay({
        questionSubmitted: 0,
        questionApproved: 0,
        questionReturned: 0
    });
    
    updateTasksDisplay([]);
    updateActivitiesDisplay([]);
}

// Backward compatibility functions (keeping original function names)
function loadDashboardData() {
    initializeLecturerDashboard();
}

function loadStats() {
    loadStatsWithScope();
}

function loadTasks() {
    loadTasksWithScope();
}

function loadActivities() {
    loadActivitiesWithScope();
}
