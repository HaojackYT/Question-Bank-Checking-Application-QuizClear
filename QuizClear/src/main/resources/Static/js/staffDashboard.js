document.addEventListener('DOMContentLoaded', function() {
    // Initialize scope-aware dashboard
    loadStaffDashboardWithScope();
});

// Global scope variables for dashboard filtering
let currentUserScope = {
    userId: null,
    userRole: null,
    accessibleDepartmentIds: [],
    accessibleSubjectIds: [],
    canViewAllData: false
};

// Load staff dashboard with scope filtering
async function loadStaffDashboardWithScope() {
    try {
        // First, get user scope
        const scopeResponse = await fetch('/api/user/current-scope', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (scopeResponse.ok) {
            currentUserScope = await scopeResponse.json();
            console.log('Staff dashboard scope initialized:', currentUserScope);
        }

        // Load dashboard data with scope parameters
        const params = new URLSearchParams();
        if (currentUserScope.accessibleDepartmentIds.length > 0) {
            params.append('departmentIds', currentUserScope.accessibleDepartmentIds.join(','));
        }
        if (currentUserScope.accessibleSubjectIds.length > 0) {
            params.append('subjectIds', currentUserScope.accessibleSubjectIds.join(','));
        }
        params.append('requestingUserId', currentUserScope.userId);
        params.append('userRole', currentUserScope.userRole);

        const dashboardResponse = await fetch(`/api/dashboard/staff?${params}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (dashboardResponse.ok) {
            const data = await dashboardResponse.json();
            updateDashboardDisplay(data);
        } else {
            console.error('Failed to load staff dashboard data');
            loadFallbackDashboard();
        }
    } catch (error) {
        console.error('Error loading staff dashboard:', error);
        loadFallbackDashboard();
    }
}

// Update dashboard display with scope-filtered data
function updateDashboardDisplay(data) {
    // Update overview statistics
    updateStatistics(data);
    
    // Update charts with filtered data
    updateBarChart(data.barChart);
    updateLineChart(data.lineChart);
    
    // Update recent activities
    updateRecentActivities(data.recentActivities);
    
    // Add scope indicator
    updateScopeIndicator(data.scopeInfo);
}

// Update statistics cards with scope awareness
function updateStatistics(data) {
    const statCards = document.querySelectorAll('.stat-card');
    
    if (statCards[0]) {
        statCards[0].querySelector('.stat-value').textContent = data.totalSubjects || 0;
        statCards[0].querySelector('.stat-change').textContent = `+${data.subjectsThisMonth || 0} this month`;
        
        // Add scope indicator if filtered
        if (!currentUserScope.canViewAllData && currentUserScope.accessibleSubjectIds.length > 0) {
            const scopeIndicator = statCards[0].querySelector('.scope-indicator') || document.createElement('div');
            scopeIndicator.className = 'scope-indicator';
            scopeIndicator.textContent = `(${currentUserScope.accessibleSubjectIds.length} accessible)`;
            if (!statCards[0].querySelector('.scope-indicator')) {
                statCards[0].appendChild(scopeIndicator);
            }
        }
    }
    
    if (statCards[1]) {
        statCards[1].querySelector('.stat-value').textContent = data.totalQuestions || 0;
        statCards[1].querySelector('.stat-change').textContent = `+${data.questionsThisMonth || 0} this month`;
    }
    
    if (statCards[2]) {
        statCards[2].querySelector('.stat-value').textContent = data.duplicateQuestions || 0;
        statCards[2].querySelector('.stat-change').textContent = 'Needs review';
    }
    
    if (statCards[3]) {
        statCards[3].querySelector('.stat-value').textContent = data.examsCreated || 0;
        statCards[3].querySelector('.stat-change').textContent = `+${data.examsThisMonth || 0} this month`;
    }
}

// Update bar chart with scope-filtered data
function updateBarChart(chartData) {
    if (window.barChart && typeof window.barChart.destroy === 'function') {
        window.barChart.destroy();
    }
    
    const barCtx = document.getElementById('barChart').getContext('2d');
    window.barChart = new Chart(barCtx, {
        type: 'bar',
        data: {
            labels: chartData.labels.map(() => ''), // Hide X-axis labels
            datasets: chartData.datasets.map(ds => ({
                label: ds.label + (currentUserScope.canViewAllData ? '' : ' (Filtered)'),
                data: ds.data,
                backgroundColor: ds.backgroundColor,
                borderRadius: 6,
                barThickness: 32
            }))
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            layout: { padding: { top: 24, bottom: 0, left: 0, right: 0 } },
            plugins: {
                legend: {
                    display: true,
                    position: 'bottom',
                    labels: {
                        boxWidth: 18,
                        boxHeight: 18,
                        font: { size: 16, weight: 'bold' },
                        color: '#3B2175',
                        padding: 24
                    }
                },
                title: { 
                    display: !currentUserScope.canViewAllData,
                    text: 'Data filtered by your scope',
                    font: { size: 12 },
                    color: '#666'
                },
                tooltip: {
                    enabled: true,
                    callbacks: {
                        afterBody: function(context) {
                            if (!currentUserScope.canViewAllData) {
                                return ['', 'Note: Data filtered by your accessible departments/subjects'];
                            }
                            return '';
                        }
                    }
                }
            },
            scales: {
                x: { display: false },
                y: {
                    beginAtZero: true,
                    grid: { display: true, color: '#f0f0f0' },
                    ticks: { font: { size: 14 }, color: '#666' }
                }
            }
        }
    });
}

// Update line chart with scope awareness
function updateLineChart(chartData) {
    if (window.lineChart && typeof window.lineChart.destroy === 'function') {
        window.lineChart.destroy();
    }

    const lineCtx = document.getElementById('lineChart').getContext('2d');
    window.lineChart = new Chart(lineCtx, {
        type: 'line',
        data: {
            labels: chartData.labels,
            datasets: chartData.datasets.map(ds => ({
                ...ds,
                label: ds.label + (currentUserScope.canViewAllData ? '' : ' (Filtered)')
            }))
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: true, position: 'bottom' },
                title: { 
                    display: !currentUserScope.canViewAllData,
                    text: 'Trends based on your accessible data',
                    font: { size: 12 },
                    color: '#666'
                }
            },
            scales: {
                x: { display: true },
                y: { beginAtZero: true }
            }
        }
    });
}

// Update recent activities with scope filtering
function updateRecentActivities(activities) {
    const activitiesContainer = document.getElementById('recent-activities');
    if (!activitiesContainer) return;

    activitiesContainer.innerHTML = '';
    
    // Filter activities by scope
    const filteredActivities = activities.filter(activity => isActivityInScope(activity));
    
    if (filteredActivities.length === 0) {
        activitiesContainer.innerHTML = '<p class="no-activities">No recent activities in your scope</p>';
        return;
    }

    filteredActivities.slice(0, 10).forEach(activity => {
        const activityElement = document.createElement('div');
        activityElement.className = 'activity-item';
        activityElement.innerHTML = `
            <div class="activity-icon ${activity.type}"></div>
            <div class="activity-content">
                <p class="activity-text">${activity.description}</p>
                <span class="activity-time">${formatActivityTime(activity.timestamp)}</span>
            </div>
        `;
        activitiesContainer.appendChild(activityElement);
    });
}

// Check if activity is within user scope
function isActivityInScope(activity) {
    if (currentUserScope.canViewAllData) return true;
    
    // Check department scope
    if (activity.departmentId && currentUserScope.accessibleDepartmentIds.length > 0) {
        if (!currentUserScope.accessibleDepartmentIds.includes(activity.departmentId)) {
            return false;
        }
    }
    
    // Check subject scope
    if (activity.subjectId && currentUserScope.accessibleSubjectIds.length > 0) {
        if (!currentUserScope.accessibleSubjectIds.includes(activity.subjectId)) {
            return false;
        }
    }
    
    return true;
}

// Update scope indicator in dashboard header
function updateScopeIndicator(scopeInfo) {
    let scopeIndicator = document.getElementById('dashboard-scope-indicator');
    if (!scopeIndicator) {
        scopeIndicator = document.createElement('div');
        scopeIndicator.id = 'dashboard-scope-indicator';
        scopeIndicator.className = 'scope-indicator-header';
        
        const header = document.querySelector('.dashboard-header') || document.querySelector('h1');
        if (header) {
            header.parentNode.insertBefore(scopeIndicator, header.nextSibling);
        }
    }
    
    if (currentUserScope.canViewAllData) {
        scopeIndicator.textContent = 'Viewing all data';
        scopeIndicator.className = 'scope-indicator-header all-access';
    } else {
        const deptCount = currentUserScope.accessibleDepartmentIds.length;
        const subjCount = currentUserScope.accessibleSubjectIds.length;
        scopeIndicator.textContent = `Filtered view: ${deptCount} departments, ${subjCount} subjects`;
        scopeIndicator.className = 'scope-indicator-header filtered';
    }
}

// Format activity timestamp
function formatActivityTime(timestamp) {
    const date = new Date(timestamp);
    const now = new Date();
    const diffMs = now - date;
    const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
    const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));
    
    if (diffHours < 1) return 'Just now';
    if (diffHours < 24) return `${diffHours}h ago`;
    if (diffDays < 7) return `${diffDays}d ago`;
    return date.toLocaleDateString();
}

// Fallback dashboard for when API fails
function loadFallbackDashboard() {
    // Implement fallback logic, e.g., load cached data, show error message, etc.
    console.warn('Loading fallback dashboard data');
    document.querySelectorAll('.stat-card').forEach(card => {
        card.querySelector('.stat-value').textContent = 'N/A';
        card.querySelector('.stat-change').textContent = '';
    });
    
    if (window.barChart) {
        window.barChart.destroy();
        document.getElementById('barChart').getContext('2d').clearRect(0, 0, 400, 400);
    }
    
    if (window.lineChart) {
        window.lineChart.destroy();
        document.getElementById('lineChart').getContext('2d').clearRect(0, 0, 400, 400);
    }
    
    const activitiesContainer = document.getElementById('recent-activities');
    if (activitiesContainer) {
        activitiesContainer.innerHTML = '<p class="no-activities">Unable to load activities</p>';
    }
}
