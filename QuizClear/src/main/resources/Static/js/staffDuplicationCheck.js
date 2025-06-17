const API_URLS = {
    duplications: '/api/staff/duplications',
    filterOptions: '/api/staff/duplications/filters',
    filteredDuplications: '/api/staff/duplications/filtered',
    dupContent: '/staff/dup-content', // Match StaffViewController
    statContent: '/staff/stat-content',
    logContent: '/staff/log-content',
    dupDetails: '/staff/dup-details', // Update this to match the new route
    logDetails: '/api/staff/duplications' // Consider aligning with a specific log endpoint
};

function loadCSS(href) {
    let link = document.querySelector("link[data-tab-css]");
    if (link) {
        link.href = href;
    } else {
        link = document.createElement("link");
        link.rel = "stylesheet";
        link.href = href;
        link.setAttribute("data-tab-css", "");
        document.head.appendChild(link);
    }
}

function removeCSS(href) {
    const links = document.querySelectorAll('link[rel="stylesheet"]');
    links.forEach(link => {
        if (link.href.includes(href)) {
            link.remove();
        }
    });
}

function bindTabEvents() {
    const tabs = document.querySelectorAll(".tab");
    if (tabs.length === 0) {
        console.warn("No tab elements found to bind events");
        return;
    }
    
    tabs.forEach((tab) => {
        if (tab && tab.addEventListener) {
            tab.addEventListener("click", () => {
                if (tab.dataset && tab.dataset.tab !== 'detection') {
                    window._detectionTabLoaded = false;
                }
                handleTabClick(tab);
            });
        }
    });
}

function handleTabClick(tab) {
    if (!tab || !tab.classList) {
        console.error("Tab element is null or has no classList");
        return;
    }
    
    const tabs = document.querySelectorAll(".tab");
    if (!tabs || tabs.length === 0) {
        console.error("No tab elements found");
        return;
    }
    
    // Remove active class from all tabs
    tabs.forEach((t) => {
        if (t && t.classList) {
            t.classList.remove("active");
        }
    });
    
    // Add active class to clicked tab
    tab.classList.add("active");

    const tabName = tab.dataset ? tab.dataset.tab : null;
    if (!tabName) {
        console.error("Tab name not found");
        return;
    }
    
    const contentArea = document.getElementById("tab-content");
    if (!contentArea) {
        console.error("Content area not found");
        return;
    }
    
    const tabConfig = {
        detection: { file: API_URLS.dupContent, css: "/css/staff/staffDup.css" },
        stat: { file: API_URLS.statContent, css: "/css/staff/staffStats.css" },
        proc_log: { file: API_URLS.logContent, css: "/css/staff/staffLogs.css" }
    };
    
    const { file, css } = tabConfig[tabName] || {};
    if (file) {
        fetch(file, { headers: { 'Accept': 'text/html' } })
            .then((res) => {
                if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
                return res.text();
            })            .then((html) => {
                contentArea.innerHTML = html;
                if (css) loadCSS(css);
                bindInnerEvents();
                if (tabName === 'detection') {
                    bindFilterEvents();
                    // Chỉ gọi loadDuplications khi lần đầu vào tab hoặc reload tab, KHÔNG gọi lại sau khi lọc
                    if (!window._detectionTabLoaded) {
                        loadDuplications();
                        window._detectionTabLoaded = true;
                    }
                }
            })
            .catch((err) => {
                contentArea.innerHTML = "<p>Error loading content: " + err.message + "</p>";
                console.error("Error loading tab content:", err);
            });
    }
}

// Load filter options from API
async function loadFilterOptions() {
    try {
        console.log("Loading filter options...");
        const response = await fetch(API_URLS.filterOptions);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        console.log("Filter options loaded:", data);
        // Populate subject filter
        const subjectSelect = document.querySelector('.filter-select.subject');
        if (subjectSelect && data.subjects) {
            // Lưu lại giá trị đang chọn
            const prevValue = subjectSelect.value;
            subjectSelect.innerHTML = '<option value="">All Subjects</option>';
            data.subjects.forEach(subject => {
                const option = document.createElement('option');
                option.value = subject;
                option.textContent = subject;
                subjectSelect.appendChild(option);
            });
            // Khôi phục lại lựa chọn trước đó nếu còn tồn tại
            if (prevValue && [...subjectSelect.options].some(opt => opt.value === prevValue)) {
                subjectSelect.value = prevValue;
            } else {
                subjectSelect.value = '';
            }
        }
        // Populate submitter filter
        const submitterSelect = document.querySelector('.filter-select.submitter');
        if (submitterSelect && data.submitters) {
            const prevValue = submitterSelect.value;
            submitterSelect.innerHTML = '<option value="">All Submitters</option>';
            data.submitters.forEach(submitter => {
                const option = document.createElement('option');
                option.value = submitter;
                option.textContent = submitter;
                submitterSelect.appendChild(option);
            });
            if (prevValue && [...submitterSelect.options].some(opt => opt.value === prevValue)) {
                submitterSelect.value = prevValue;
            } else {
                submitterSelect.value = '';
            }
        }
        console.log("Filter options populated successfully");
    } catch (error) {
        console.error('Error loading filter options:', error);
    }
}

// Apply filters and reload data
async function applyFilters() {
    try {
        const subjectSelect = document.querySelector('.filter-select.subject');
        const submitterSelect = document.querySelector('.filter-select.submitter');
        
        const selectedSubject = subjectSelect ? subjectSelect.value : '';
        const selectedSubmitter = submitterSelect ? submitterSelect.value : '';
        
        console.log("Applying filters:", { subject: selectedSubject, submitter: selectedSubmitter });
        
        // Build query parameters
        const params = new URLSearchParams();
        if (selectedSubject) params.append('subject', selectedSubject);
        if (selectedSubmitter) params.append('submitter', selectedSubmitter);
        
        const url = `${API_URLS.filteredDuplications}?${params.toString()}`;
        console.log("Fetching filtered data from:", url);
        
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const filteredData = await response.json();
        console.log("Filtered data received:", filteredData.length, "items");
        
        // Display filtered data
        displayDuplications(filteredData);
        
    } catch (error) {
        console.error('Error applying filters:', error);
        alert('Error applying filters. Please try again.');
    }
}

// Bind filter events
function bindFilterEvents() {
    const subjectSelect = document.querySelector('.filter-select.subject');
    const submitterSelect = document.querySelector('.filter-select.submitter');
    if (subjectSelect) {
        subjectSelect.onchange = null;
        subjectSelect.addEventListener('change', function(e) {
            // Đảm bảo option được chọn thực sự thay đổi
            console.log('Subject changed:', subjectSelect.value);
            applyFilters();
        });
    }
    if (submitterSelect) {
        submitterSelect.onchange = null;
        submitterSelect.addEventListener('change', function(e) {
            console.log('Submitter changed:', submitterSelect.value);
            applyFilters();
        });
    }
    console.log("Filter events bound successfully");
}

async function loadDuplications() {
    try {
        // Load filter options first
        await loadFilterOptions();
        
        // Gọi API thật để lấy dữ liệu từ database
        const response = await fetch(API_URLS.duplications, {
            headers: { 'Accept': 'application/json' }
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }        const data = await response.json();
        console.log('Loaded data from database:', data);
        
        // Manual debug of first item
        if (data && data.length > 0) {
            const first = data[0];
            console.log('=== FIRST ITEM DEBUG ===');
            console.log('detectionId:', first.detectionId);
            console.log('newQuestion:', first.newQuestion);
            console.log('similarQuestion:', first.similarQuestion);
            console.log('similarityScore:', first.similarityScore);
            console.log('status:', first.status);
            if (first.newQuestion) {
                console.log('newQuestion.content:', first.newQuestion.content);
                console.log('newQuestion.courseName:', first.newQuestion.courseName);
                console.log('newQuestion.creatorName:', first.newQuestion.creatorName);
            }
            console.log('========================');
        }
        
        // Check if data is valid before displaying
        if (data && Array.isArray(data) && data.length > 0) {
            displayDuplications(data);
        } else {
            console.warn('Invalid data received from API:', data);
            throw new Error('Invalid data structure');
        }
          } catch (err) {
        console.error('Error loading duplications:', err);        
        // Show error message instead of mock data
        console.error('Failed to load duplication data from API');
        showDuplicationError('Failed to load data from server. Please try again.');
    }
}

// Show error message when duplication data loading fails
function showDuplicationError(message) {
    const container = document.querySelector('.duplications-container') || document.querySelector('#duplications-list') || document.body;
    const errorDiv = document.createElement('div');
    errorDiv.className = 'alert alert-danger text-center';
    errorDiv.innerHTML = `<i class="fas fa-exclamation-triangle"></i> ${message}`;
    container.appendChild(errorDiv);
}

function displayDuplications(duplications) {
    console.log('displayDuplications called with:', duplications.length, 'items');
    const tbody = document.querySelector("table tbody");
    console.log('Found tbody element:', tbody);
    if (tbody) {
        tbody.innerHTML = duplications.map(d => {
            // Debug each item
            console.log('Processing item:', d.detectionId, d);
            if (!d.detectionId) {
                console.warn('WARNING: detectionId is missing for item:', d);
            }
            return `
            <tr>
                <td class="question-cell">${d.newQuestion?.content || 'N/A'}</td>
                <td class="question-cell">${d.similarQuestion?.content || 'N/A'}</td>
                <td>
                    <div class="${d.similarityScore >= 0.9 ? 'similarity-complete' : 'similarity-high'} similarity-badge">
                        ${d.similarityScore >= 0.9 ? 'Complete Duplicate' : 'High Similarity'} (${(d.similarityScore * 100).toFixed(1)}%)
                    </div>
                </td>
                <td><div class="subject-badge">${d.newQuestion?.courseName || 'N/A'}</div></td>                <td class="submitter-name">${d.newQuestion?.creatorName || 'N/A'}</td>
                <td class="action-buttons">
                    <button class="action-icon view-icon" data-detection-id="${d.detectionId}" 
                            ${d.status === 'INVALID' ? 'disabled' : ''} 
                            title="${d.status === 'INVALID' ? 'Detection lỗi, không thể xem chi tiết' : 'View Details'}"
                            onclick="viewDuplication(this)">🔍</button>
                    <button class="action-icon accept-icon" data-detection-id="${d.detectionId}"
                            ${d.status === 'INVALID' ? 'disabled' : ''} 
                            title="Accept Question"
                            onclick="acceptDuplication(this)">✅</button>
                    <button class="action-icon reject-icon" data-detection-id="${d.detectionId}"
                            ${d.status === 'INVALID' ? 'disabled' : ''} 
                            title="Reject Question"
                            onclick="rejectDuplication(this)">❌</button>
                </td>
            </tr>
        `;
        }).join('');
        console.log('Data displayed successfully in table');
        
        // Bind View buttons after rendering table
        setTimeout(() => {
            bindViewButtons();
        }, 50);
    } else {
        console.warn('tbody not found when displaying duplications');
    }
}

function exportDuplications() {
    const tbody = document.querySelector("table tbody");
    if (!tbody) return;
    const rows = Array.from(tbody.querySelectorAll("tr")).map(row => {
        const cells = row.querySelectorAll("td");
        return [
            cells[0].textContent,
            cells[1].textContent,
            cells[2].textContent,
            cells[3].textContent,
            cells[4].textContent
        ].join(",");
    }).join("\n");
    const csv = "New Question,Similar To,Similarity,Subjects,Submitter\n" + rows;
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = "duplications.csv";
    a.click();
    window.URL.revokeObjectURL(url);
}

function exportLogs() {
    const tbody = document.querySelector("table tbody");
    if (!tbody) return;
    const rows = Array.from(tbody.querySelectorAll("tr")).map(row => {
        const cells = row.querySelectorAll("td");
        return [
            cells[0].textContent,
            cells[1].textContent,
            cells[2].textContent,
            cells[3].textContent,
            cells[4].textContent,
            cells[5].textContent,
            cells[6].textContent
        ].join(",");
    }).join("\n");
    const csv = "Log ID,Question,Duplicate,Similarity,Action,Processor,Date\n" + rows;
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = "logs.csv";
    a.click();
    window.URL.revokeObjectURL(url);
}

function filterLogs(search) {
    const tbody = document.querySelector("table tbody");
    if (!tbody) return;
    const rows = tbody.querySelectorAll("tr");
    rows.forEach(row => {
        const text = Array.from(row.querySelectorAll("td")).map(cell => cell.textContent.toLowerCase()).join(" ");
        row.style.display = text.includes(search.toLowerCase()) ? "" : "none";
    });
}

async function showLogDetails(logId) {
    try {
        const response = await fetch(`${API_URLS.logDetails}/${logId.replace("L", "")}`, {
            headers: { 'Accept': 'application/json' }
        });
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const log = await response.json();
        const modalContent = document.getElementById("logDetailContent");
        modalContent.innerHTML = `
            <div class="info-row"><span class="info-label">Log ID:</span><span class="info-value">L${log.id}</span></div>
            <div class="info-row"><span class="info-label">New Question:</span><span class="info-value">${log.newQuestion?.content || 'N/A'}</span></div>
            <div class="info-row"><span class="info-label">Similar Question:</span><span class="info-value">${log.similarQuestion.content}</span></div>
            <div class="info-row"><span class="info-label">Similarity:</span><span class="info-value">${(log.similarityScore * 100).toFixed(1)}%</span></div>
            <div class="info-row"><span class="info-label">Action:</span><span class="info-value">${log.action || 'N/A'}</span></div>
            <div class="info-row"><span class="info-label">Processor:</span><span class="info-value">${log.processedBy?.name || 'N/A'}</span></div>
            <div class="info-row"><span class="info-label">Date:</span><span class="info-value">${log.processedAt ? new Date(log.processedAt).toLocaleString() : 'N/A'}</span></div>
            <div class="info-row"><span class="info-label">Feedback:</span><span class="info-value">${log.feedback || 'N/A'}</span></div>
        `;
        document.getElementById("logDetailModal").style.display = "flex";
    } catch (err) {
        console.error('Error loading log details:', err);
        alert('Failed to load log details: ' + err.message);
    }
}

// Ensure filter events are bound and filter options loaded after content is loaded
function bindInnerEvents() {
    // Bind filter events
    const filterSelects = document.querySelectorAll(".filter-select");
    filterSelects.forEach((select) => {
        select.addEventListener("change", () => applyFilters());
    });
    
    // Bind View buttons for detection tab
    bindViewButtons();
    
    console.log("Inner events bound successfully");
}

function bindGlobalEvents() {
    document.addEventListener("click", function (e) {
        const target = e.target;
        if (target && target.classList) {
            if (target.classList.contains("btn-view-duplication")) {
                e.preventDefault();
                const detectionId = target.getAttribute('data-detection-id');
                loadDuplicationDetails(detectionId);
            } else if (target.classList.contains("back-link")) {
                e.preventDefault();
                goBackToDetectionList();
            } else if (target.classList.contains("modal-overlay")) {
                // Close modal when clicking on overlay (outside modal content)
                if (target.id === "logDetailModal") {
                    closeModal();
                }
            }
        }
    });
    
    // Handle Escape key to close modals
    document.addEventListener("keydown", function(e) {
        if (e.key === "Escape") {
            const logModal = document.getElementById("logDetailModal");
            
            if (logModal && logModal.style.display === "flex") {
                closeModal();
            }
        }
    });
}

function ensureDetailCSSLoaded() {
    const cssFiles = [
        '/css/staff/staffDupDetails-scoped.css',
        '/css/staff/staffDup.css'
    ];
    cssFiles.forEach(href => {
        if (!document.querySelector(`link[href*='${href}']`)) {
            const link = document.createElement('link');
            link.rel = 'stylesheet';
            link.href = href;
            document.head.appendChild(link);
        }
    });
}

async function loadDuplicationDetails(detectionId) {
    console.log("loadDuplicationDetails called with detectionId:", detectionId);
    
    if (!detectionId || detectionId === '{id}') {
        console.error("Invalid detectionId:", detectionId);
        alert("Error: Invalid detection ID");
        return;
    }
    
    try {        console.log("Making AJAX request to:", `/staff/dup-details/${detectionId}`);
        const response = await fetch(`/staff/dup-details/${detectionId}`);
        
        console.log("AJAX response status:", response.status);
        if (!response.ok) throw new Error('Failed to load details');
        
        const html = await response.text();        console.log("AJAX response received, HTML length:", html.length);
          // Replace the current tab content with the details
        const tabContent = document.getElementById('tab-content');
        if (tabContent) {
            tabContent.innerHTML = html;
            
            // Load the staffDupDetails CSS
            loadCSS('/css/staff/staffDupDetails.css');
            
            // Mark that we're in details view
            window._inDetailsView = true;
            
            console.log("Details content replaced tab content");
            
            // Bind form submission for the details content
            bindDetailsFormEvents();
        } else {
            console.error("tab-content element not found");
        }
    } catch (err) {
        console.error('Error loading details:', err);
        alert('Error loading details: ' + err.message);
    }
}

// Sửa lại sự kiện nút View để gọi loadDuplicationDetails thay vì chuyển trang
function bindViewButtons() {
    console.log("bindViewButtons called - binding View button events");
    const viewButtons = document.querySelectorAll('.btn-view-duplication');
    console.log("Found View buttons:", viewButtons.length);
    
    viewButtons.forEach((btn, index) => {
        const detectionId = btn.getAttribute('data-detection-id');
        console.log(`Binding button ${index + 1}, detectionId:`, detectionId);
        
        btn.onclick = function(event) {
            event.preventDefault(); // Chặn mọi hành động mặc định (chuyển trang)
            console.log("View button clicked, detectionId:", detectionId);
            loadDuplicationDetails(detectionId);
        };
    });
}

// Action button handlers
function viewDuplication(button) {
    const detectionId = button.getAttribute('data-detection-id');
    console.log("View duplication:", detectionId);
    loadDuplicationDetails(detectionId);
}

function acceptDuplication(button) {
    const detectionId = button.getAttribute('data-detection-id');
    if (confirm('Are you sure you want to accept this question?')) {
        processDuplicationAction(detectionId, 'ACCEPT');
    }
}

function rejectDuplication(button) {
    const detectionId = button.getAttribute('data-detection-id');
    if (confirm('Are you sure you want to reject this question?')) {
        processDuplicationAction(detectionId, 'REJECT');
    }
}

async function processDuplicationAction(detectionId, action) {
    try {
        const response = await fetch(`/api/staff/duplications/${detectionId}/process`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `action=${action}&feedback=${action === 'ACCEPT' ? 'Question accepted' : 'Question rejected'}`
        });
        
        if (response.ok) {
            alert(`Question ${action.toLowerCase()}ed successfully!`);
            // Reload the duplications list
            loadDuplications();
        } else {
            throw new Error(`Failed to ${action.toLowerCase()} question`);
        }
    } catch (error) {
        console.error('Error processing action:', error);
        alert(`Error ${action.toLowerCase()}ing question. Please try again.`);
    }
}

function closeModal() {
    document.getElementById("logDetailModal").style.display = "none";
}

// Bind form events for details content (not modal)
function bindDetailsFormEvents() {
    const form = document.querySelector('#tab-content form');
    if (form) {
        form.addEventListener('submit', async function(e) {
            e.preventDefault();
            
            const formData = new FormData(form);
            const actionUrl = form.action;
            
            try {
                const response = await fetch(actionUrl, {
                    method: 'POST',
                    body: formData
                });
                
                if (response.ok) {
                    alert('Action processed successfully!');
                    // Go back to the detection tab content
                    goBackToDetectionList();
                } else {
                    alert('Error processing action. Please try again.');
                }
            } catch (error) {
                console.error('Error submitting form:', error);
                alert('Error submitting form. Please try again.');
            }
        });
    }
}

// Function to go back to detection list
function goBackToDetectionList() {
    console.log("goBackToDetectionList called");
    
    // Remove the staffDupDetails CSS
    removeCSS('/css/staff/staffDupDetails.css');
    
    // Reset details view flag
    window._inDetailsView = false;
    
    // Load the detection tab content directly instead of using handleTabClick
    const contentArea = document.getElementById("tab-content");
    if (contentArea) {
        fetch(API_URLS.dupContent, { headers: { 'Accept': 'text/html' } })
            .then((res) => {
                if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
                return res.text();
            })
            .then((html) => {
                contentArea.innerHTML = html;
                loadCSS("/css/staff/staffDup.css");
                
                // Bind events for the loaded content
                bindFilterEvents();
                
                // Load duplications data
                loadDuplications();
                
                console.log("Detection list loaded successfully");
            })
            .catch((err) => {
                console.error("Error loading detection list:", err);
                contentArea.innerHTML = "<p>Error loading content: " + err.message + "</p>";
            });
    } else {
        console.error("Content area not found");
    }
}

document.addEventListener("DOMContentLoaded", () => {
    bindTabEvents();
    bindGlobalEvents(); // ĐẢM BẢO SỰ KIỆN CLICK VIEW ĐƯỢC GẮN
    
    // Make goBackToDetectionList globally available
    window.goBackToDetectionList = goBackToDetectionList;
    
    // Optionally, load the first tab by default
    const firstTab = document.querySelector('.tab');
    if (firstTab) handleTabClick(firstTab);
});