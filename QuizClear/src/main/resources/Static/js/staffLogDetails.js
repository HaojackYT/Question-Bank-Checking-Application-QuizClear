// Staff Log Details JavaScript
document.addEventListener('DOMContentLoaded', function() {
    console.log('Log details page loaded');
    
    // Get log ID from URL
    const logId = getLogIdFromURL();
    if (logId) {
        loadLogDetails(logId);
    } else {
        console.error('No log ID found in URL');
        showError('Invalid log ID');
    }
});

function getLogIdFromURL() {
    const path = window.location.pathname;
    const matches = path.match(/\/staff\/log-details\/(\d+)/);
    return matches ? matches[1] : null;
}

async function loadLogDetails(logId) {
    try {
        console.log('Loading details for log ID:', logId);
        
        // Show loading state
        showLoadingState();
        
        // Fetch log details from API
        const response = await fetch(`/api/staff/duplications/processing-logs/${logId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const log = await response.json();
        
        if (!log) {
            throw new Error('Log not found');
        }
        
        // Populate the details
        populateLogDetails(log);
        
    } catch (error) {
        console.error('Error loading log details:', error);
        showError('Failed to load log details: ' + error.message);
    }
}

function showLoadingState() {
    document.getElementById('log-id').textContent = 'Loading...';
    document.getElementById('log-action').textContent = 'Loading...';
    document.getElementById('log-processor').textContent = 'Loading...';
    document.getElementById('log-date').textContent = 'Loading...';
    document.getElementById('new-question-content').textContent = 'Loading question content...';
    document.getElementById('similar-question-content').textContent = 'Loading question content...';
    document.getElementById('similarity-score').querySelector('.percentage').textContent = '...%';
}

function populateLogDetails(log) {
    try {
        // Basic log info
        document.getElementById('log-id').textContent = log.logId || 'N/A';
        
        // Action badge
        const actionElement = document.getElementById('log-action');
        const action = log.action || 'UNKNOWN';
        actionElement.textContent = action;
        actionElement.className = 'info-value action-badge ' + action.toLowerCase();
        
        document.getElementById('log-processor').textContent = log.processorName || 'Unknown';
        document.getElementById('log-date').textContent = formatDateTime(log.processedAt) || 'N/A';
        
        // Questions content
        document.getElementById('new-question-content').textContent = log.newQuestion || 'Question content not available';
        document.getElementById('similar-question-content').textContent = log.similarQuestion || 'Question content not available';
        
        // Courses and creators
        document.getElementById('new-question-course').textContent = log.newQuestionCourse || 'Unknown Course';
        document.getElementById('similar-question-course').textContent = log.similarQuestionCourse || 'Unknown Course';
        document.getElementById('new-question-creator').textContent = log.newQuestionCreator || 'Unknown Creator';
        document.getElementById('similar-question-creator').textContent = log.similarQuestionCreator || 'Unknown Creator';
        
        // Similarity
        const similarity = log.similarity || '0%';
        const percentageElement = document.getElementById('similarity-score').querySelector('.percentage');
        percentageElement.textContent = similarity;
        
        // Update similarity bar
        const similarityValue = parseFloat(similarity.replace('%', ''));
        const fillElement = document.getElementById('similarity-fill');
        fillElement.style.width = Math.min(similarityValue, 100) + '%';
        
        // Update similarity color based on value
        if (similarityValue >= 90) {
            percentageElement.style.color = '#dc3545'; // Red for high similarity
            fillElement.style.background = '#dc3545';
        } else if (similarityValue >= 75) {
            percentageElement.style.color = '#ffc107'; // Yellow for medium similarity
            fillElement.style.background = '#ffc107';
        } else {
            percentageElement.style.color = '#28a745'; // Green for low similarity
            fillElement.style.background = '#28a745';
        }
        
        // Feedback and activity
        document.getElementById('log-feedback').textContent = log.feedback || 'No feedback provided';
        document.getElementById('log-activity').textContent = log.activity || 'No activity details available';
        
        // Action summary
        updateActionSummary(action, log);
        
    } catch (error) {
        console.error('Error populating log details:', error);
        showError('Error displaying log details');
    }
}

function updateActionSummary(action, log) {
    const summaryCard = document.getElementById('action-summary-card');
    const summaryIcon = document.getElementById('summary-icon');
    const summaryTitle = document.getElementById('summary-title');
    const summaryDescription = document.getElementById('summary-description');
    
    // Remove existing action classes
    summaryCard.className = 'summary-card';
    
    switch (action.toUpperCase()) {
        case 'ACCEPTED':
            summaryCard.classList.add('accepted');
            summaryIcon.innerHTML = '✓';
            summaryTitle.textContent = 'Detection Accepted';
            summaryDescription.textContent = 'The new question was accepted despite similarity. Both questions serve different purposes or target different learning outcomes.';
            break;
            
        case 'REJECTED':
            summaryCard.classList.add('rejected');
            summaryIcon.innerHTML = '✗';
            summaryTitle.textContent = 'Detection Rejected';
            summaryDescription.textContent = 'The new question was rejected due to high similarity with existing content. The question was considered duplicate or redundant.';
            break;
            
        case 'SEND_BACK':
            summaryCard.classList.add('send_back');
            summaryIcon.innerHTML = '↩';
            summaryTitle.textContent = 'Sent Back for Revision';
            summaryDescription.textContent = 'The question was sent back to the author for revision. Modifications are needed to reduce similarity or improve uniqueness.';
            break;
            
        default:
            summaryIcon.innerHTML = '?';
            summaryTitle.textContent = 'Unknown Action';
            summaryDescription.textContent = 'The processing action is not recognized or recorded.';
    }
}

function showError(message) {
    // Show error in all main content areas
    const errorMessage = `<div style="color: #dc3545; text-align: center; padding: 20px;">${message}</div>`;
    
    document.getElementById('log-id').innerHTML = 'Error';
    document.getElementById('log-action').innerHTML = 'Error';
    document.getElementById('log-processor').innerHTML = 'Error';
    document.getElementById('log-date').innerHTML = 'Error';
    document.getElementById('new-question-content').innerHTML = errorMessage;
    document.getElementById('similar-question-content').innerHTML = errorMessage;
}

function formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return 'N/A';
    
    try {
        const date = new Date(dateTimeStr);
        return date.toLocaleString('en-CA', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: false
        }).replace(',', '');
    } catch (e) {
        console.warn('Date formatting error:', e);
        return dateTimeStr;
    }
}

// Navigation functions
function goBackToProcessingLogs() {
    // Navigate back to the duplication page with logs tab selected
    window.location.href = '/staff/duplications?tab=proc_log';
}

// Make functions globally available
window.goBackToProcessingLogs = goBackToProcessingLogs;

// Export function
function exportLogDetails() {
    try {
        const logData = {
            logId: document.getElementById('log-id').textContent,
            action: document.getElementById('log-action').textContent,
            processor: document.getElementById('log-processor').textContent,
            date: document.getElementById('log-date').textContent,
            similarity: document.getElementById('similarity-score').querySelector('.percentage').textContent,
            newQuestion: document.getElementById('new-question-content').textContent,
            similarQuestion: document.getElementById('similar-question-content').textContent,
            newQuestionCourse: document.getElementById('new-question-course').textContent,
            similarQuestionCourse: document.getElementById('similar-question-course').textContent,
            newQuestionCreator: document.getElementById('new-question-creator').textContent,
            similarQuestionCreator: document.getElementById('similar-question-creator').textContent,
            feedback: document.getElementById('log-feedback').textContent,
            activity: document.getElementById('log-activity').textContent
        };

        const content = `Processing Log Details Report
===============================

Log Information:
- Log ID: ${logData.logId}
- Action Taken: ${logData.action}
- Processed By: ${logData.processor}
- Processing Date: ${logData.date}
- Similarity Score: ${logData.similarity}

Question Comparison:
-------------------

New Question:
Course: ${logData.newQuestionCourse}
Created By: ${logData.newQuestionCreator}
Content: ${logData.newQuestion}

Similar Question:
Course: ${logData.similarQuestionCourse}
Created By: ${logData.similarQuestionCreator}
Content: ${logData.similarQuestion}

Processing Details:
------------------

Feedback: ${logData.feedback}

Full Activity Log: ${logData.activity}

Report generated on: ${new Date().toLocaleString()}
`;

        const blob = new Blob([content], { type: 'text/plain;charset=utf-8' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `processing_log_${logData.logId}_${new Date().toISOString().split('T')[0]}.txt`;
        a.click();
        window.URL.revokeObjectURL(url);
        
        console.log('Log details exported successfully');
        
    } catch (error) {
        console.error('Error exporting log details:', error);
        alert('Failed to export log details: ' + error.message);
    }
}

// Make export function globally available
window.exportLogDetails = exportLogDetails;
