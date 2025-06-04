document.addEventListener('DOMContentLoaded', function() {
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');
    const editQuestionButtons = document.querySelectorAll('.edit-question-btn');
    const editQuestionForm = document.getElementById('edit-question-form');
    const backToRevisionsButton = document.getElementById('back-to-revisions');

    const mainContentTitle = document.getElementById('main-content-title');
    const mainContentTabs = document.getElementById('main-content-tabs');
    const editFormFeedbackText = document.getElementById('edit-form-feedback-text');

    let activeTabId = 'revisions-content';
    let currentQuestionFeedback = '';

    // Function to show a specific content area and update title/tabs
    function showContent(contentType) {
        tabContents.forEach(content => content.classList.remove('active-tab-content'));
        editQuestionForm.style.display = 'none';

        if (contentType === 'tabs') {
            mainContentTitle.textContent = 'Feedback & Revisions';
            mainContentTabs.style.display = 'flex';
            document.getElementById(activeTabId).classList.add('active-tab-content'); // Show the previously active tab
        } else if (contentType === 'edit-form') {
            mainContentTitle.textContent = 'Edit Question';
            mainContentTabs.style.display = 'none';
            editQuestionForm.style.display = 'block';
            editFormFeedbackText.textContent = currentQuestionFeedback;
    }


    // Event listener for tab buttons
    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            mainContentTitle.textContent = 'Feedback & Revisions';
            mainContentTabs.style.display = 'flex';
            editQuestionForm.style.display = 'none'; 
            tabButtons.forEach(btn => btn.classList.remove('active'));
            tabContents.forEach(content => content.classList.remove('active-tab-content'));

            
            button.classList.add('active');
            activeTabId = button.dataset.tab + '-content';
            document.getElementById(activeTabId).classList.add('active-tab-content');
        });
    });

    // Event listener for "Edit Question" buttons
    editQuestionButtons.forEach(button => {
        button.addEventListener('click', () => {
        
            const feedbackBox = button.closest('.feedback-item').querySelector('.feedback-box p');
            if (feedbackBox) {
                currentQuestionFeedback = feedbackBox.textContent;
            } else {
                currentQuestionFeedback = 'No specific feedback for this question.';
            }

            showContent('edit-form');
        });
    });

    // Event listener for "Back" button in Edit Question form
    backToRevisionsButton.addEventListener('click', () => {
        showContent('tabs');

        tabButtons.forEach(btn => {
            btn.classList.remove('active');
            if (btn.dataset.tab === 'revisions') {
                btn.classList.add('active');
            }
        });
        activeTabId = 'revisions-content';
    });


    document.getElementById('revisions-content').classList.add('active-tab-content');
    document.querySelector('.tab-button[data-tab="revisions"]').classList.add('active');
    document.getElementById('feedback-history-content').classList.remove('active-tab-content');
    document.querySelector('.tab-button[data-tab="feedback-history"]').classList.remove('active');


    showContent('tabs');
    }
});