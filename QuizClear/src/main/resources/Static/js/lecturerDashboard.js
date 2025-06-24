// Lecturer Dashboard JavaScript
document.addEventListener('DOMContentLoaded', function() {
    loadDashboardData();
});

function loadDashboardData() {
    console.log('=== Loading dashboard data ===');
    
    // Load real data from API first
    loadStats();
    loadTasks();
    loadActivities();
}

function loadStats() {
    console.log('=== Loading stats from API ===');
    
    // Use real API endpoint (assuming user is logged in)
    fetch('/api/dashboard/lecturer/stats')
        .then(response => {
            console.log('Stats API response status:', response.status);
            if (!response.ok) {
                throw new Error('Failed to fetch stats: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log('*** REAL Stats data received:', data);
            updateStatsDisplay(data);
        })
        .catch(error => {
            console.error('Error loading stats:', error);
            // Fallback to test endpoint if session-based fails
            fetch('/api/dashboard/lecturer/test-stats')
                .then(response => response.json())
                .then(data => {
                    console.log('*** TEST Stats data received:', data);
                    updateStatsDisplay(data);
                })
                .catch(err => {
                    console.error('Both APIs failed:', err);
                    updateStatsDisplay({
                        questionSubmitted: 0,
                        questionApproved: 0,
                        questionReturned: 0
                    });
                });
        });
}

function updateStatsDisplay(stats) {
    const submittedElement = document.querySelector('.stat-card:nth-child(1) .stat-value');
    const approvedElement = document.querySelector('.stat-card:nth-child(2) .stat-value');
    const returnedElement = document.querySelector('.stat-card:nth-child(3) .stat-value');
    
    if (submittedElement) submittedElement.textContent = stats.questionSubmitted;
    if (approvedElement) approvedElement.textContent = stats.questionApproved;
    if (returnedElement) returnedElement.textContent = stats.questionReturned;
}

function loadTasks() {
    console.log('=== Loading tasks from API ===');
    
    // Use real API endpoint
    fetch('/api/dashboard/lecturer/tasks')
        .then(response => {
            console.log('Tasks API response status:', response.status);
            if (!response.ok) {
                throw new Error('Failed to fetch tasks: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log('*** REAL Tasks data received:', data);
            updateTasksDisplay(data);
        })
        .catch(error => {
            console.error('Error loading tasks:', error);
            // Fallback to test endpoint
            fetch('/api/dashboard/lecturer/test-tasks')
                .then(response => response.json())
                .then(data => {
                    console.log('*** TEST Tasks data received:', data);
                    updateTasksDisplay(data);
                })
                .catch(err => {
                    console.error('Both task APIs failed:', err);
                    updateTasksDisplay([]);
                });
        });
}

function updateTasksDisplay(tasks) {
    const assignmentSection = document.querySelector('.assignment-section');
    const existingTasks = assignmentSection.querySelectorAll('.assignment-item');
    
    // Always remove existing static tasks first
    existingTasks.forEach(task => task.remove());
    
    if (tasks && tasks.length > 0) {
        tasks.forEach(task => {
            const taskElement = createTaskElement(task);
            assignmentSection.appendChild(taskElement);
        });
    } else {
        // If no data, show message instead of keeping static data
        const noDataDiv = document.createElement('div');
        noDataDiv.className = 'assignment-item';
        noDataDiv.innerHTML = '<p>Không có nhiệm vụ nào được giao.</p>';
        assignmentSection.appendChild(noDataDiv);
    }
}

function createTaskElement(task) {
    const taskDiv = document.createElement('div');
    taskDiv.className = 'assignment-item';
    
    const progressWidth = Math.min(100, Math.max(0, task.progressPercentage));
    
    taskDiv.innerHTML = `
        <div class="assignment-header">
            <span>${task.title}</span>
            <span class="count">${task.completedQuestions}/${task.totalQuestions} sentence </span>
        </div>
        <div class="progress-bar">
            <div class="progress-fill" style="width: ${progressWidth}%;"></div>
        </div>
        <p class="deadline">Deadline: ${task.deadline}</p>
    `;
    
    return taskDiv;
}

function loadActivities() {
    console.log('=== Loading activities from API ===');
    
    // Use real API endpoint
    fetch('/api/dashboard/lecturer/activities')
        .then(response => {
            console.log('Activities API response status:', response.status);
            if (!response.ok) {
                throw new Error('Failed to fetch activities: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log('*** REAL Activities data received:', data);
            updateActivitiesDisplay(data);
        })
        .catch(error => {
            console.error('Error loading activities:', error);
            // Fallback to test endpoint
            fetch('/api/dashboard/lecturer/test-activities')
                .then(response => response.json())
                .then(data => {
                    console.log('*** TEST Activities data received:', data);
                    updateActivitiesDisplay(data);
                })
                .catch(err => {
                    console.error('Both activity APIs failed:', err);
                    updateActivitiesDisplay([]);
                });
        });
}

function updateActivitiesDisplay(activities) {
    const activitySection = document.querySelector('.activity');
    const existingActivities = activitySection.querySelectorAll('.activity-item');
    
    // Always remove existing static activities first
    existingActivities.forEach(activity => activity.remove());
    
    if (activities && activities.length > 0) {
        activities.forEach(activity => {
            const activityElement = createActivityElement(activity);
            activitySection.appendChild(activityElement);
        });
    } else {
        // If no data, show message instead of keeping static data
        const noDataDiv = document.createElement('div');
        noDataDiv.className = 'activity-item';
        noDataDiv.innerHTML = '<p>Chưa có hoạt động gần đây.</p>';
        activitySection.appendChild(noDataDiv);
    }
}

function createActivityElement(activity) {
    const activityDiv = document.createElement('div');
    activityDiv.className = 'activity-item';
    
    activityDiv.innerHTML = `
        <div class="activity-info">
            <p class="activity-text"><strong>${activity.activityText}</strong></p>
            <p class="activity-caption">${activity.activityCaption}</p>
        </div>
        <div class="${activity.statusClass}">${activity.status}</div>
        <div class="date">${activity.date}</div>
    `;
    
    return activityDiv;
}

// Auto refresh every 30 seconds
setInterval(loadDashboardData, 30000);
