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
    try {
        if (!href) {
            console.warn('loadCSS: href is empty');
            return;
        }
        
        console.log('Loading CSS:', href);
        let link = document.querySelector("link[data-tab-css]");
        if (link) {
            link.href = href;
            console.log('Updated existing CSS link');
        } else {
            link = document.createElement("link");
            link.rel = "stylesheet";
            link.href = href;
            link.setAttribute("data-tab-css", "");
            
            // Add error handling for CSS loading
            link.onerror = function() {
                console.warn('Failed to load CSS:', href);
            };
            
            link.onload = function() {
                console.log('Successfully loaded CSS:', href);
            };
            
            document.head.appendChild(link);
            console.log('Added new CSS link');
        }
    } catch (error) {
        console.error('Error in loadCSS:', error);
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
        
        if (!subjectSelect || !submitterSelect) {
            console.error('Filter select elements not found');
            return;
        }
        
        const selectedSubject = subjectSelect.value;
        const selectedSubmitter = submitterSelect.value;
        
        console.log("Applying filters:", { subject: selectedSubject, submitter: selectedSubmitter });
        
        // Build query parameters
        const params = new URLSearchParams();
        if (selectedSubject && selectedSubject.trim() !== '') {
            params.append('subject', selectedSubject.trim());
        }
        if (selectedSubmitter && selectedSubmitter.trim() !== '') {
            params.append('submitter', selectedSubmitter.trim());
        }
        
        const url = `${API_URLS.filteredDuplications}?${params.toString()}`;
        console.log("Fetching filtered data from:", url);
        
        // Show loading indicator
        const tbody = document.querySelector('.table tbody');
        if (tbody) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 20px;">Loading...</td></tr>';
        }
        
        const response = await fetch(url);
        console.log("Filter response status:", response.status);
        
        if (!response.ok) {
            const errorText = await response.text();
            console.error("Filter error response:", errorText);
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const filteredData = await response.json();
        console.log("Filtered data received:", filteredData.length, "items");
        
        // Display filtered data
        displayDuplications(filteredData);
        
    } catch (error) {
        console.error('Error applying filters:', error);
        
        // Show error in table
        const tbody = document.querySelector('.table tbody');
        if (tbody) {
            tbody.innerHTML = `<tr><td colspan="6" style="text-align: center; padding: 20px; color: red;">
                Error loading filtered data: ${error.message}
                <br><small>Check console for details</small>
            </td></tr>`;
        }
        
        alert('Error applying filters: ' + error.message);
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
        if (data && Array.isArray(data)) {
            if (data.length > 0) {
                displayDuplications(data);
            } else {
                // Empty array is valid - no pending detections
                console.log('No pending detections found');
                displayDuplications([]); // Display empty list
            }
        } else {
            console.warn('Invalid data received from API:', data);
            throw new Error('Invalid data structure');
        }
          } catch (err) {
        console.error('Error loading duplications:', err);
        
        // Fallback to mock data if API fails
        console.warn('USING MOCK DATA - API call failed!');
        const mockData = [
            {
                detectionId: 1,
                newQuestion: {
                    questionId: 1,
                    content: "What is the purpose of a UML diagram in software engineering?",
                    courseName: "Software Engineering"
                },
                similarQuestion: {
                    questionId: 2,
                    content: "Explain the role of UML diagrams in system design?",
                    courseName: "Software Engineering"
                },
                similarityScore: 0.95,
                status: "PENDING",
                detectedBy: {
                    userId: 1,
                    fullName: "Alexander Brooks"
                },
                detectedAt: "2025-06-14T10:30:00"
            }
        ];
        displayDuplications(mockData);
    }
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
        
        try {
            if (target && target.classList) {
                if (target.classList.contains("btn-view-duplication")) {
                    e.preventDefault();
                    const detectionId = target.getAttribute('data-detection-id');
                    if (detectionId) {
                        loadDuplicationDetails(detectionId);
                    } else {
                        console.error('No detection ID found on view button');
                    }
                } else if (target.classList.contains("back-link")) {
                    e.preventDefault();
                    goBackToDetectionList();
                } else if (target.classList.contains("modal-overlay")) {
                    // Close modal when clicking on overlay (outside modal content)
                    if (target.id === "logDetailModal") {
                        closeModal();
                    }
                }
            } else {
                // Only log if the target is an interactive element (button, link, etc)
                if (target && (target.tagName === 'BUTTON' || target.tagName === 'A' || target.onclick)) {
                    console.warn('Interactive element missing classList:', target);
                }
            }
        } catch (error) {
            console.error('Error in global click handler:', error);
        }
    });
    
    // Handle Escape key to close modals
    document.addEventListener("keydown", function(e) {
        try {
            if (e.key === "Escape") {
                const logModal = document.getElementById("logDetailModal");
                
                if (logModal && logModal.style.display === "flex") {
                    closeModal();
                }
            }
        } catch (error) {
            console.error('Error in keydown handler:', error);
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

// Action button handlers with better error handling
function viewDuplication(button) {
    try {
        if (!button) {
            console.error('viewDuplication: button is null');
            return;
        }
        
        const detectionId = button.getAttribute('data-detection-id');
        if (!detectionId) {
            console.error('viewDuplication: detection ID not found');
            return;
        }
        
        console.log("View duplication:", detectionId);
        loadDuplicationDetails(detectionId);
    } catch (error) {
        console.error('Error in viewDuplication:', error);
    }
}

// Hàm xử lý Accept trực tiếp từ bảng danh sách
async function acceptDuplication(button) {
    const detectionId = button.getAttribute('data-detection-id');
    
    if (!detectionId) {
        alert('Detection ID not found');
        return;
    }
    
    // Xác nhận action
    if (!confirm('Are you sure you want to ACCEPT this duplicate detection?')) {
        return;
    }
    
    try {
        // Disable button và show loading
        button.disabled = true;
        button.innerHTML = '⏳';
        
        const response = await fetch(`/api/staff/duplications/${detectionId}/process`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                action: 'ACCEPT',
                feedback: '',
                processorId: 1 // Hoặc lấy từ session
            })
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const result = await response.json();
        console.log('Accept successful:', result);
        
        // Show success message
        showSuccessMessage('Detection accepted successfully!');
        
        // Reload danh sách để cập nhật
        await loadDuplications();
        
    } catch (error) {
        console.error('Error accepting duplicate:', error);
        alert('Failed to accept duplicate: ' + error.message);
        
        // Restore button
        button.disabled = false;
        button.innerHTML = '✅';
    }
}

// Hàm xử lý Reject trực tiếp từ bảng danh sách
async function rejectDuplication(button) {
    const detectionId = button.getAttribute('data-detection-id');
    
    if (!detectionId) {
        alert('Detection ID not found');
        return;
    }
    
    // Xác nhận action
    if (!confirm('Are you sure you want to REJECT this duplicate detection?')) {
        return;
    }
    
    try {
        // Disable button và show loading
        button.disabled = true;
        button.innerHTML = '⏳';
        
        const response = await fetch(`/api/staff/duplications/${detectionId}/process`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                action: 'REJECT',
                feedback: '',
                processorId: 1 // Hoặc lấy từ session
            })
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const result = await response.json();
        console.log('Reject successful:', result);
        
        // Show success message
        showSuccessMessage('Detection rejected successfully!');
        
        // Reload danh sách để cập nhật
        await loadDuplications();
        
    } catch (error) {
        console.error('Error rejecting duplicate:', error);
        alert('Failed to reject duplicate: ' + error.message);
        
        // Restore button
        button.disabled = false;
        button.innerHTML = '❌';
    }
}

// Hàm hiển thị thông báo thành công
function showSuccessMessage(message) {
    // Tạo toast notification
    const toast = document.createElement('div');
    toast.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: #4CAF50;
        color: white;
        padding: 15px 20px;
        border-radius: 5px;
        box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        z-index: 10000;
        font-family: Arial, sans-serif;
        font-size: 14px;
    `;
    toast.textContent = message;
    
    document.body.appendChild(toast);
    
    // Tự động xóa sau 3 giây
    setTimeout(() => {
        if (toast.parentNode) {
            toast.parentNode.removeChild(toast);
        }
    }, 3000);
}

function closeModal() {
    document.getElementById("logDetailModal").style.display = "none";
}

// Bind form events for details content (not modal)
function bindDetailsFormEvents() {
    console.log("bindDetailsFormEvents called");
    
    const form = document.querySelector('#tab-content form');
    if (form) {
        console.log("Found form in tab-content, binding events");
          form.addEventListener('submit', async function(e) {
            e.preventDefault();
              const formData = new FormData(form);
            const action = formData.get('action');
            const feedback = formData.get('feedback');
            const processorId = formData.get('processorId');
            const actionUrl = form.getAttribute('action'); // Sửa: dùng getAttribute thay vì .action
            
            console.log("Form submission:", { action, feedback, processorId, actionUrl });
            console.log("Form data entries:");
            for (let [key, value] of formData.entries()) {
                console.log(`  ${key}: ${value}`);
            }
              if (!action) {
                alert('Please select an action before submitting.');
                return;
            }
            
            // Validate action values
            const validActions = ['ACCEPT', 'REJECT', 'SEND_BACK'];
            if (!validActions.includes(action)) {
                alert('Invalid action selected: ' + action);
                return;
            }
            
            // Validate processorId
            if (!processorId || isNaN(parseInt(processorId))) {
                alert('Invalid processor ID: ' + processorId);
                return;
            }
              if (confirm(`Are you sure you want to ${getActionDisplayName(action)} this detection?`)) {
                let originalText = 'Submit Action'; // Declare in outer scope
                  try {
                    // Show loading state
                    const submitButton = form.querySelector('button[type="submit"]');
                    if (submitButton) {
                        originalText = submitButton.textContent;
                        submitButton.textContent = 'Processing...';
                        submitButton.disabled = true;
                    }                    // Convert FormData to JSON
                    const jsonData = {
                        action: action,
                        feedback: feedback,
                        processorId: processorId ? parseInt(processorId) : null
                    };
                      console.log("Sending JSON data:", jsonData);
                    console.log("Target URL:", actionUrl);
                    console.log("Request headers:", {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json',
                        'X-Requested-With': 'XMLHttpRequest'
                    });
                    
                    const response = await fetch(actionUrl, {
                        method: 'POST',
                        body: JSON.stringify(jsonData),
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json',
                            'X-Requested-With': 'XMLHttpRequest'
                        }
                    });
                    
                    console.log("Form response status:", response.status);
                    console.log("Form response headers:", response.headers);
                    
                    if (response.ok) {
                        const contentType = response.headers.get('content-type');
                        if (contentType && contentType.includes('application/json')) {
                            const result = await response.json();
                            console.log("Form response data:", result);
                            
                            if (result.success !== false) {
                                alert('Detection processed successfully!');
                                goBackToDetectionList();
                            } else {
                                throw new Error(result.error || 'Unknown error occurred');
                            }
                        } else {
                            // Handle non-JSON response
                            const textResponse = await response.text();
                            console.log("Non-JSON response:", textResponse);
                            if (textResponse.includes('success') || response.status === 200) {
                                alert('Detection processed successfully!');
                                goBackToDetectionList();
                            } else {
                                throw new Error('Unexpected response format');
                            }
                        }
                    } else {
                        const errorText = await response.text();
                        console.error("Form response error:", errorText);
                        
                        // Try to parse as JSON first
                        try {
                            const errorJson = JSON.parse(errorText);
                            throw new Error(errorJson.error || `Server error: ${response.status}`);
                        } catch (parseError) {
                            throw new Error(`Server error: ${response.status} - ${errorText.substring(0, 200)}`);
                        }
                    }
                } catch (error) {
                    console.error('Error submitting form:', error);
                    alert('Error processing detection: ' + error.message);                } finally {
                    // Reset button state
                    const submitButton = form.querySelector('button[type="submit"]');
                    if (submitButton) {
                        submitButton.textContent = originalText;
                        submitButton.disabled = false;
                    }
                }
            }
        });
        
        console.log("Form events bound successfully");
    } else {
        console.warn("Form not found in tab-content");
    }
}

// Helper function to get action display name
function getActionDisplayName(action) {
    switch(action) {
        case 'ACCEPT': return 'accept';
        case 'REJECT': return 'reject';
        case 'SEND_BACK': return 'send back for revision';
        default: return action.toLowerCase();
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

// Test function để debug
async function testEndpoint() {
    try {
        const testData = {
            action: "ACCEPT",
            feedback: "test feedback",
            processorId: 1
        };
        
        console.log("Testing endpoint with data:", testData);
        
        const response = await fetch('/api/staff/duplications/test-endpoint', {
            method: 'POST',
            body: JSON.stringify(testData),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            }
        });
        
        console.log("Test response status:", response.status);
        
        if (response.ok) {
            const result = await response.json();
            console.log("Test response data:", result);
            alert("Test endpoint SUCCESS: " + JSON.stringify(result));
        } else {
            const errorText = await response.text();
            console.error("Test response error:", errorText);
            alert("Test endpoint FAILED: " + response.status + " - " + errorText.substring(0, 200));
        }
    } catch (error) {
        console.error('Test endpoint error:', error);
        alert('Test endpoint ERROR: ' + error.message);
    }
}

// Thêm nút test vào window object để có thể gọi từ console
window.testEndpoint = testEndpoint;

document.addEventListener("DOMContentLoaded", () => {
    console.log("DOM Content Loaded - initializing...");
    
    // Add error handling for missing elements
    try {
        bindTabEvents();
        bindGlobalEvents(); // ĐẢM BẢO SỰ KIỆN CLICK VIEW ĐƯỢC GẮN
        
        // Make goBackToDetectionList globally available
        window.goBackToDetectionList = goBackToDetectionList;
        
        // Optionally, load the first tab by default
        const firstTab = document.querySelector('.tab');
        if (firstTab) {
            console.log("Loading first tab:", firstTab.dataset?.tab);
            handleTabClick(firstTab);
        } else {
            console.warn("No tabs found on page load");
        }
    } catch (error) {
        console.error("Error during DOMContentLoaded:", error);
    }
});

// Export functions to global scope để có thể gọi từ HTML
window.acceptDuplication = acceptDuplication;
window.rejectDuplication = rejectDuplication;
window.viewDuplication = viewDuplication;