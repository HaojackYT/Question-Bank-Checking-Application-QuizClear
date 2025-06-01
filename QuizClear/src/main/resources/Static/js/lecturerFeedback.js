document.addEventListener('DOMContentLoaded', function() {
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');
    const editQuestionButtons = document.querySelectorAll('.edit-question-btn');
    const editQuestionForm = document.getElementById('edit-question-form');
    const backToRevisionsButton = document.getElementById('back-to-revisions');

    const mainContentTitle = document.getElementById('main-content-title');
    const mainContentTabs = document.getElementById('main-content-tabs');
    const editFormFeedbackText = document.getElementById('edit-form-feedback-text');

    let activeTabId = 'revisions-content'; // Mặc định khi vào trang này, Revisions là tab được chọn
    let currentQuestionFeedback = ''; // Biến để lưu trữ feedback của câu hỏi được chỉnh sửa

    // Function to show a specific content area and update title/tabs
    function showContent(contentType) {
        // Hide all main content areas (tabs and form)
        tabContents.forEach(content => content.classList.remove('active-tab-content'));
        editQuestionForm.style.display = 'none';

        // Show relevant content
        if (contentType === 'tabs') {
            mainContentTitle.textContent = 'Feedback & Revisions';
            mainContentTabs.style.display = 'flex';
            document.getElementById(activeTabId).classList.add('active-tab-content'); // Show the previously active tab
        } else if (contentType === 'edit-form') {
            mainContentTitle.textContent = 'Edit Question';
            mainContentTabs.style.display = 'none';
            editQuestionForm.style.display = 'block';
            editFormFeedbackText.textContent = currentQuestionFeedback; // Update feedback in form
        }
    }


    // Event listener for tab buttons
    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            // First, ensure we are in tab view
            mainContentTitle.textContent = 'Feedback & Revisions';
            mainContentTabs.style.display = 'flex';
            editQuestionForm.style.display = 'none'; // Hide edit form if it was open

            // Remove active class from all buttons and hide all content
            tabButtons.forEach(btn => btn.classList.remove('active'));
            tabContents.forEach(content => content.classList.remove('active-tab-content'));

            // Add active class to the clicked button
            button.classList.add('active');

            // Show the corresponding tab content
            activeTabId = button.dataset.tab + '-content'; // Update activeTabId
            document.getElementById(activeTabId).classList.add('active-tab-content');
        });
    });

    // Event listener for "Edit Question" buttons
    editQuestionButtons.forEach(button => {
        button.addEventListener('click', () => {
            // In a real application, you'd fetch question data here
            // For this example, we'll get the feedback text from the sibling feedback-box
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
        // Ensure the 'Revisions' tab is active when returning
        tabButtons.forEach(btn => {
            btn.classList.remove('active');
            if (btn.dataset.tab === 'revisions') {
                btn.classList.add('active');
            }
        });
        activeTabId = 'revisions-content'; // Reset active tab to Revisions
    });

    // Initialize display based on which tab should be active by default
    // (Assuming 'Feedback History' is active when the page loads, but 'Revisions' is the target for Edit)
    // Set the revisions tab as active initially for correct behavior after returning from edit.
    document.getElementById('revisions-content').classList.add('active-tab-content');
    document.querySelector('.tab-button[data-tab="revisions"]').classList.add('active');
    // Ensure other tab is not active initially
    document.getElementById('feedback-history-content').classList.remove('active-tab-content');
    document.querySelector('.tab-button[data-tab="feedback-history"]').classList.remove('active');

    // Initial state: show tabs (revisions by default as per the problem implies editing it)
    showContent('tabs');
});