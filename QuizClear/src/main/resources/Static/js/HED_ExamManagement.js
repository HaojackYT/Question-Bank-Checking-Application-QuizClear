document.addEventListener('DOMContentLoaded', function() {
    initializeSearchFilter();
    initializeTableActions();
    initializePagination();
    initializeNotifications();
    initializeExamDetailsModal();
});

// Search and Filter
function initializeSearchFilter() {
    const searchInput = document.querySelector('.search-input');
    const searchBtn = document.querySelector('.search-btn');
    const filterSelects = document.querySelectorAll('.filter-select');

    if (searchBtn) {
        searchBtn.addEventListener('click', performSearch);
    }
    
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                performSearch();
            }
        });
    }

    filterSelects.forEach(select => {
        select.addEventListener('change', applyFilters);
    });
}

function performSearch() {
    const searchInput = document.querySelector('.search-input');
    const searchTerm = searchInput ? searchInput.value.toLowerCase() : '';
    const tableRows = document.querySelectorAll('.exam-table tbody tr');
    
    tableRows.forEach(row => {
        const rowText = row.textContent.toLowerCase();
        row.style.display = rowText.includes(searchTerm) ? '' : 'none';
    });
}

function applyFilters() {
    const statusFilter = document.querySelector('.filter-select:first-of-type')?.value || '';
    const subjectFilter = document.querySelector('.filter-select:last-of-type')?.value || '';
    const tableRows = document.querySelectorAll('.exam-table tbody tr');
    
    tableRows.forEach(row => {
        let showRow = true;
        
        // Status filter
        if (statusFilter) {
            const statusBadge = row.querySelector('.status-badge');
            if (statusBadge) {
                const statusText = statusBadge.textContent.toLowerCase().replace(/\s+/g, '');
                if (statusText !== statusFilter) {
                    showRow = false;
                }
            }
        }
        
        // Subject filter
        if (subjectFilter && showRow && row.cells.length > 1) {
            const subjectCell = row.cells[1].textContent.toLowerCase();
            if (!subjectCell.includes(subjectFilter.toLowerCase())) {
                showRow = false;
            }
        }
        
        row.style.display = showRow ? '' : 'none';
    });
}

// Table Actions
function initializeTableActions() {
    const actionButtons = document.querySelectorAll('.action-btn');
    
    actionButtons.forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const action = this.classList.contains('view-btn') ? 'view' : 
                          this.classList.contains('edit-btn') ? 'edit' : 'delete';
            const row = this.closest('tr');
            if (row && row.cells.length > 0) {
                const examId = row.cells[0].textContent;
                handleTableAction(action, examId, row);
            }
        });
    });
}

function handleTableAction(action, examId, row) {
    switch(action) {
        case 'view':
            alert(`Viewing exam ${examId}`);
            break;
        case 'edit':
            const statusBadge = row.querySelector('.status-badge');
            if (statusBadge) {
                const status = statusBadge.textContent.toLowerCase().trim();
                if (status === 'approved') {
                    alert('Cannot edit approved exams');
                    return;
                }
            }
            alert(`Editing exam ${examId}`);
            break;
        case 'delete':
            if (confirm('Are you sure you want to delete this exam?')) {
                row.remove();
                alert('Exam deleted successfully');
            }
            break;
    }
}

// Pagination
function initializePagination() {
    const prevBtn = document.querySelector('.pagination-btn:first-of-type');
    const nextBtn = document.querySelector('.pagination-btn:last-of-type');
    
    if (prevBtn) {
        prevBtn.addEventListener('click', () => changePage(-1));
    }
    if (nextBtn) {
        nextBtn.addEventListener('click', () => changePage(1));
    }
}

function changePage(direction) {
    const currentPageSpan = document.querySelector('.pagination-current');
    if (!currentPageSpan) return;
    
    const currentPage = parseInt(currentPageSpan.textContent);
    const totalPages = 9;
    
    let newPage = currentPage + direction;
    if (newPage < 1) newPage = 1;
    if (newPage > totalPages) newPage = totalPages;
    
    currentPageSpan.textContent = newPage;
    
    // Update button states
    const prevBtn = document.querySelector('.pagination-btn:first-of-type');
    const nextBtn = document.querySelector('.pagination-btn:last-of-type');
    
    if (prevBtn) prevBtn.disabled = newPage === 1;
    if (nextBtn) nextBtn.disabled = newPage === totalPages;
}

// Notifications
function initializeNotifications() {
    const checkboxes = document.querySelectorAll('.notification-checkbox input');
    const seeMoreLink = document.querySelector('.see-more-link');
    
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            const notificationItem = this.closest('.notification-item');
            if (notificationItem) {
                notificationItem.style.opacity = this.checked ? '0.6' : '1';
            }
        });
    });
    
    if (seeMoreLink) {
        seeMoreLink.addEventListener('click', function(e) {
            e.preventDefault();
            alert('Loading more notifications...');
        });
    }
}

// Exam Details Modal Handler
function initializeExamDetailsModal() {
    const modal = document.getElementById('examDetailsModal');
    const viewButtons = document.querySelectorAll('.view-btn');
    const closeBtn = modal?.querySelector('.close-btn');
    const expandBtn = modal?.querySelector('.expand-btn');
    const rejectBtn = modal?.querySelector('.reject-btn');
    const approveBtn = modal?.querySelector('.approve-btn');

    // Sample exam data
    const examData = {
        'A1001': {
            lecturer: 'Nguyen Van A - 1001',
            title: 'Midterm exam of the Operating System subject',
            totalQuestion: 30,
            subject: 'Operating System',
            deadline: '---',
            status: 'In Progress',
            description: 'This comprehensive exam covers process management, memory management, file systems, and synchronization concepts in operating systems.',
            dueDate: '2025-07-15'
        },
        'A1002': {
            lecturer: 'Tran Thi B - 1002',
            title: 'Final exam of the Database subject',
            totalQuestion: 25,
            subject: 'Database',
            deadline: '2:00 PM',
            status: 'Approved',
            description: 'Advanced database concepts including normalization, query optimization, transaction management, and database security.',
            dueDate: '2025-06-20'
        },
        'A1003': {
            lecturer: 'Vo Van C - 1003',
            title: 'Midterm exam of the Computer Architecture subject',
            totalQuestion: 30,
            subject: 'Computer Architecture',
            deadline: '8:00 AM',
            status: 'Pending',
            description: 'Computer architecture fundamentals covering CPU design, memory hierarchy, instruction sets, and performance optimization.',
            dueDate: '2025-08-10'
        },
        'A1004': {
            lecturer: 'Ho Thi D - 1004',
            title: 'Final exam of the Database Management System',
            totalQuestion: 20,
            subject: 'Database Management System',
            deadline: '---',
            status: 'In Progress',
            description: 'Advanced topics in database management including distributed databases, data warehousing, and NoSQL systems.',
            dueDate: '2025-09-05'
        },
        'A1005': {
            lecturer: 'Huynh E - 1005',
            title: 'Midterm exam of the Object Oriented Programming subject',
            totalQuestion: 15,
            subject: 'Object Oriented Programming',
            deadline: '---',
            status: 'In Progress',
            description: 'Object-oriented programming concepts including inheritance, polymorphism, encapsulation, and design patterns.',
            dueDate: '2025-25-05'
        }
        // Add more exam data as needed
    };

    // Open modal when view button is clicked
    viewButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const row = this.closest('tr');
            const examId = row.cells[0].textContent.trim();
            const exam = examData[examId];
            
            if (exam && modal) {
                // Populate modal with exam data
                modal.querySelector('#modalLecturer').textContent = exam.lecturer;
                modal.querySelector('#modalTitle').textContent = exam.title;
                modal.querySelector('#modalTotalQuestion').textContent = exam.totalQuestion;
                modal.querySelector('#modalSubject').textContent = exam.subject;
                modal.querySelector('#modalDeadline').textContent = exam.deadline;
                modal.querySelector('#modalDescription').textContent = exam.description;
                modal.querySelector('#modalDueDate').textContent = exam.dueDate;
                
                // Update status badge
                const statusElement = modal.querySelector('#modalStatus');
                statusElement.textContent = exam.status;
                statusElement.className = 'status-badge ' + getStatusClass(exam.status);
                
                // Show modal
                modal.style.display = 'block';
                document.body.style.overflow = 'hidden';
            }
        });
    });

    // Close modal
    function closeModal() {
        if (modal) {
            modal.style.display = 'none';
            document.body.style.overflow = 'auto';
        }
    }

    closeBtn?.addEventListener('click', closeModal);

    // Close modal when clicking outside
    window.addEventListener('click', function(e) {
        if (e.target === modal) {
            closeModal();
        }
    });

    // Close modal with ESC key
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && modal.style.display === 'block') {
            closeModal();
        }
    });

    // Expand modal
    expandBtn?.addEventListener('click', function() {
        modal.querySelector('.modal-content').classList.toggle('expanded');
    });

    // Reject button
    rejectBtn?.addEventListener('click', function() {
        alert('Exam rejected!');
        closeModal();
    });

    // Approve button
    approveBtn?.addEventListener('click', function() {
        alert('Exam approved!');
        closeModal();
    });

    // Helper function to get status class
    function getStatusClass(status) {
        switch(status.toLowerCase().replace(' ', '-')) {
            case 'approved': return 'approved';
            case 'pending': return 'pending';
            case 'in-progress': return 'in-progress';
            case 'rejected': return 'rejected';
            default: return 'pending';
        }
    }
}