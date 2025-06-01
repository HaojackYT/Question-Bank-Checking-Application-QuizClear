document.addEventListener('DOMContentLoaded', function() {
    initializeSearchFilter();
    initializeTableActions();
    initializePagination();
    initializeNotifications();
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