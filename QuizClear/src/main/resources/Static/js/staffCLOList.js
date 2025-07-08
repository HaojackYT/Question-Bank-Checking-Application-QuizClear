// CLO Management JavaScript Functions

// Load recent activities on page load
document.addEventListener('DOMContentLoaded', function() {
    loadRecentActivities();
    setupEventListeners();
});

function setupEventListeners() {
    // Search input enter key
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                performSearch();
            }
        });
    }

    // Edit button event delegation
    const cloTable = document.querySelector('.course-table');
    if (cloTable) {
        cloTable.addEventListener('click', function(event) {
            const editButton = event.target.closest('.js-edit-button');
            if (editButton) {
                const row = editButton.closest('tr');
                const cloId = row.getAttribute('data-clo-id');
                if (cloId && cloId !== 'null') {
                    loadCLOForEdit(row); // Truyền row thay vì cloId
                } else {
                    alert('Không tìm thấy thông tin CLO');
                }
            }
        });
    }
}

// Modal functions
function openModal() {
    document.getElementById("modal").style.display = "flex";
}

function closeModal() {
    document.getElementById("modal").style.display = "none";
}

function openCreateModal() {
    document.getElementById("modal-create").style.display = "flex";
}

function closeCreateModal() {
    document.getElementById("modal-create").style.display = "none";
}

// Search functionality
function performSearch() {
    const keyword = document.getElementById('searchInput').value;
    const currentUrl = new URL(window.location);
    
    if (keyword && keyword.trim()) {
        currentUrl.searchParams.set('keyword', keyword.trim());
    } else {
        currentUrl.searchParams.delete('keyword');
    }
    currentUrl.searchParams.set('page', '0'); // Reset to first page
    window.location.href = currentUrl.toString();
}

// Filter functionality
function performFilter() {
    const difficultyLevel = document.getElementById('difficultyFilter').value;
    const currentUrl = new URL(window.location);
    
    if (difficultyLevel !== 'AllDepartment') {
        currentUrl.searchParams.set('difficultyLevel', difficultyLevel);
    } else {
        currentUrl.searchParams.delete('difficultyLevel');
    }
    currentUrl.searchParams.set('page', '0'); // Reset to first page
    window.location.href = currentUrl.toString();
}

// Create CLO
function submitCreateCLO(event) {
    event.preventDefault();
    
    const courseId = document.getElementById('createCourseId').value;
    if (!courseId || isNaN(courseId)) {
        alert('Please enter a valid Course ID');
        return;
    }
    
    const formData = {
        cloCode: document.getElementById('createCLOCode').value,
        difficultyLevel: document.getElementById('createDifficultyLevel').value,
        cloDescription: document.getElementById('createDescription').value,
        weight: document.getElementById('createWeight').value || null,
        cloNote: document.getElementById('createNotes').value,
        course: {
            courseId: parseInt(courseId)
        }
    };

    fetch('/api/clos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (response.ok) {
            closeCreateModal();
            showSuccessMessage('CLO created successfully');
            setTimeout(() => window.location.reload(), 1000);
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Error creating CLO');
            });
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error creating CLO: ' + error.message);
    });
}

// Load CLO data for editing from data attributes
function loadCLOForEdit(row) {
    const cloId = row.getAttribute('data-clo-id');
    const cloCode = row.getAttribute('data-clo-code');
    const cloDescription = row.getAttribute('data-clo-description');
    const difficultyLevel = row.getAttribute('data-difficulty-level');
    const weight = row.getAttribute('data-weight');
    const courseId = row.getAttribute('data-course-id');
    
    document.getElementById('editCLOId').value = cloId || '';
    document.getElementById('editCLOCode').value = cloCode || '';
    document.getElementById('editDifficultyLevel').value = difficultyLevel || 'Recognition';
    document.getElementById('editDescription').value = cloDescription || '';
    document.getElementById('editWeight').value = weight || '';
    document.getElementById('editCourseId').value = courseId || '';
    document.getElementById('editNotes').value = ''; // Notes không có trong data
    
    openModal();
}

// Edit CLO
function submitEditCLO(event) {
    event.preventDefault();
    
    const cloId = document.getElementById('editCLOId').value;
    const courseId = document.getElementById('editCourseId').value;
    
    if (!courseId || isNaN(courseId)) {
        alert('Please enter a valid Course ID');
        return;
    }
    
    const formData = {
        cloCode: document.getElementById('editCLOCode').value,
        difficultyLevel: document.getElementById('editDifficultyLevel').value,
        cloDescription: document.getElementById('editDescription').value,
        weight: document.getElementById('editWeight').value || null,
        cloNote: document.getElementById('editNotes').value,
        course: {
            courseId: parseInt(courseId)
        }
    };

    fetch(`/api/clos/${cloId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (response.ok) {
            closeModal();
            showSuccessMessage('CLO updated successfully');
            setTimeout(() => window.location.reload(), 1000);
        } else {
            return response.text().then(text => {
                throw new Error(text || 'Error updating CLO');
            });
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error updating CLO: ' + error.message);
    });
}

// Load recent activities
function loadRecentActivities() {
    fetch('/subject-management/api/clos/recent-activities')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to load activities');
            }
            return response.json();
        })
        .then(activities => {
            const container = document.getElementById('activities-container');
            container.innerHTML = '';
            
            if (activities.length === 0) {
                container.innerHTML = `
                    <div class="activity-item">
                        <div class="activity-info">
                            <p class="activity-text"><strong>No recent activities</strong></p>
                            <p class="activity-caption">No CLO activities in the last 30 days</p>
                        </div>
                    </div>
                `;
                return;
            }
            
            activities.forEach(activity => {
                const activityElement = document.createElement('div');
                activityElement.className = 'activity-item';
                activityElement.innerHTML = `
                    <div class="activity-info">
                        <p class="activity-text"><strong>${escapeHtml(activity.title)}</strong></p>
                        <p class="activity-caption">${escapeHtml(activity.description)}</p>
                    </div>
                    <div class="check-btn" onclick="checkActivity(${activity.id})">Check</div>
                `;
                container.appendChild(activityElement);
            });
        })
        .catch(error => {
            console.error('Error loading activities:', error);
            const container = document.getElementById('activities-container');
            container.innerHTML = `
                <div class="activity-item">
                    <div class="activity-info">
                        <p class="activity-text"><strong>Error loading activities</strong></p>
                        <p class="activity-caption">Please refresh the page to try again</p>
                    </div>
                </div>
            `;
        });
}

// Check activity
function checkActivity(activityId) {
    fetch(`/subject-management/api/clos/activities/${activityId}/check`, {
        method: 'POST'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to check activity');
        }
        return response.json();
    })
    .then(result => {
        if (result.success) {
            showSuccessMessage('Activity checked successfully');
            loadRecentActivities(); // Reload activities
        } else {
            alert('Error checking activity: ' + (result.message || 'Unknown error'));
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error checking activity: ' + error.message);
    });
}

// Utility functions
function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.replace(/[&<>"']/g, function(m) { return map[m]; });
}

function showSuccessMessage(message) {
    // Create a simple success notification
    const notification = document.createElement('div');
    notification.style.cssText = [
        'position: fixed',
        'top: 20px',
        'right: 20px',
        'background: #28a745',
        'color: white',
        'padding: 15px 20px',
        'border-radius: 5px',
        'z-index: 10000',
        'font-weight: bold'
    ].join(';') + ';';
    notification.textContent = message;
    document.body.appendChild(notification);
    
    setTimeout(() => {
        document.body.removeChild(notification);
    }, 3000);
}