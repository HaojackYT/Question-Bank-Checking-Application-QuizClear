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
        console.log('=== Loading Staff Dashboard with Scope ===');

        // Load dashboard statistics
        await loadStaffStats();
        
        // Load chart data
        await loadStaffChartData();
        
        // Load activities
        await loadStaffActivities();

        // Update scope indicator
        updateScopeIndicator();

    } catch (error) {
        console.error('Error loading staff dashboard:', error);
        loadFallbackData();
    }
}

// Load staff statistics with dynamic data
async function loadStaffStats() {
    try {
        const response = await fetch('/api/dashboard/staff/stats', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const data = await response.json();
            console.log('Staff stats received:', data);
            
            // Update scope information
            currentUserScope = {
                ...currentUserScope,
                ...data.scopeInfo
            };
            
            // Update statistics display
            updateStatsDisplay(data);
            updateDashboardDisplay(data);
        } else {
            console.error('Failed to load staff stats:', response.status);
            loadFallbackStats();
        }
    } catch (error) {
        console.error('Error loading staff stats:', error);
        loadFallbackStats();
    }
}

// Load chart data for staff dashboard
async function loadStaffChartData() {
    try {
        const response = await fetch('/api/dashboard/staff/chart-data', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const data = await response.json();
            console.log('Chart data received:', data);
            
            // Update charts
            updateBarChart(data.barChart);
            updatePieChart(data.pieChart);
        } else {
            console.error('Failed to load chart data:', response.status);
            loadFallbackChartData();
        }
    } catch (error) {
        console.error('Error loading chart data:', error);
        loadFallbackChartData();
    }
}

// Load recent activities
async function loadStaffActivities() {
    try {
        const response = await fetch('/api/dashboard/staff/activities?limit=15', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const data = await response.json();
            console.log('Activities received:', data);
            
            // Update recent activities
            updateRecentActivities(data.recentTasks);
            updateDuplicateWarnings(data.duplicateWarnings);
        } else {
            console.error('Failed to load activities:', response.status);
            loadFallbackActivities();
        }
    } catch (error) {
        console.error('Error loading activities:', error);
        loadFallbackActivities();
    }
}

// Update dashboard display with scope-filtered data
function updateStatsDisplay(data) {
    // Update overview statistics
    updateStatistics(data);
    
    // Add scope indicator
    updateScopeIndicator();
    
    console.log('Stats display updated with dynamic data');
}

// Update statistics cards with dynamic data
function updateStatistics(data) {
    const statCards = document.querySelectorAll('.stat-card');
    
    if (statCards[0]) {
        const valueElement = statCards[0].querySelector('.stat-value');
        const changeElement = statCards[0].querySelector('.stat-change');
        if (valueElement) valueElement.textContent = data.totalSubjects || 0;
        if (changeElement) changeElement.textContent = `+${data.subjectsThisMonth || 0} this month`;
        
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
        }    });
}

// Update pie chart with scope-filtered data
function updatePieChart(chartData) {
    if (window.pieChart && typeof window.pieChart.destroy === 'function') {
        window.pieChart.destroy();
    }
    
    const pieCtx = document.getElementById('pieChart').getContext('2d');
    window.pieChart = new Chart(pieCtx, {
        type: 'pie',
        data: {
            labels: chartData.labels,
            datasets: chartData.datasets.map(ds => ({
                data: ds.data,
                backgroundColor: ds.backgroundColors || ['#8979FF', '#FF928A', '#3CC3DF', '#FFAE4C']
            }))
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { 
                    display: true, 
                    position: 'right',
                    labels: {
                        font: { size: 14 },
                        color: '#3B2175'
                    }
                },
                title: { 
                    display: !currentUserScope.canViewAllData,
                    text: 'Question difficulty distribution (Filtered)',
                    font: { size: 12 },
                    color: '#666'
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
    if (diffDays < 7) return `${diffDays}d ago`;    return date.toLocaleDateString();
}

// Update duplicate warnings display
function updateDuplicateWarnings(warnings) {
    const warningsContainer = document.querySelector('.duplicate-warning-container');
    if (!warningsContainer) return;

    const existingItems = warningsContainer.querySelectorAll('.duplicate-item');
    existingItems.forEach(item => item.remove());

    if (warnings.length === 0) {
        let noWarningsMsg = warningsContainer.querySelector('.no-duplicates');
        if (!noWarningsMsg) {
            noWarningsMsg = document.createElement('div');
            noWarningsMsg.className = 'no-duplicates';
            noWarningsMsg.innerHTML = '<p>No duplicate questions found for this staff member.</p>';
            warningsContainer.appendChild(noWarningsMsg);
        }
        return;
    }

    warnings.slice(0, 5).forEach((warning, index) => {
        const warningElement = document.createElement('div');
        warningElement.className = 'duplicate-item';
        warningElement.style.top = `${14 + (index * 14)}rem`;
        warningElement.innerHTML = `
            <div class="duplicate-header">
                <div class="similarity-text">${warning.similarityPercentage}% similar</div>
                <div class="warning-icon">
                    <svg width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="#EF2A2D" stroke-width="2">
                        <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z" />
                        <path d="M12 9v4" />
                        <path d="m12 17 .01 0" />
                    </svg>
                </div>
            </div>
            <div class="question-box">
                <div class="question-text">${warning.question1}</div>
            </div>
            <div class="question-box">
                <div class="question-text">${warning.question2}</div>
            </div>
        `;
        warningsContainer.appendChild(warningElement);
    });
}

// Update dashboard display function
function updateDashboardDisplay(data) {
    // Update any additional dashboard elements
    console.log('Dashboard display updated with:', data);
}

// Fallback functions for when API calls fail
function loadFallbackData() {
    loadFallbackStats();
    loadFallbackChartData();
    loadFallbackActivities();
}

function loadFallbackStats() {
    const statCards = document.querySelectorAll('.stat-card');
    statCards.forEach(card => {
        const valueElement = card.querySelector('.stat-value');
        const changeElement = card.querySelector('.stat-change');
        if (valueElement) valueElement.textContent = '0';
        if (changeElement) changeElement.textContent = 'No data';
    });
}

function loadFallbackChartData() {
    const fallbackBarData = {
        labels: ['No Data'],
        datasets: [{
            label: 'No Data Available',
            data: [0],
            backgroundColor: ['#e0e0e0']
        }]
    };
    
    const fallbackPieData = {
        labels: ['No Data'],
        datasets: [{
            data: [100],
            backgroundColor: ['#e0e0e0']
        }]
    };
    
    updateBarChart(fallbackBarData);
    updatePieChart(fallbackPieData);
}

function loadFallbackActivities() {
    const activitiesContainer = document.getElementById('recent-activities');
    if (activitiesContainer) {
        activitiesContainer.innerHTML = '<p class="no-activities">Unable to load recent activities</p>';
    }
    
    const warningsContainer = document.querySelector('.duplicate-warning-container');
    if (warningsContainer) {
        const existingItems = warningsContainer.querySelectorAll('.duplicate-item');
        existingItems.forEach(item => item.remove());
        
        let noWarningsMsg = warningsContainer.querySelector('.no-duplicates');
        if (!noWarningsMsg) {
            noWarningsMsg = document.createElement('div');
            noWarningsMsg.className = 'no-duplicates';
            noWarningsMsg.innerHTML = '<p>Unable to load duplicate warnings</p>';
            warningsContainer.appendChild(noWarningsMsg);
        }
    }
}
