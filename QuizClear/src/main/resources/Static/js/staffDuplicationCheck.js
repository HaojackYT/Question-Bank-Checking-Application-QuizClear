const API_URLS = {
    duplications: '/api/staff/duplications',
    filterOptions: '/api/staff/duplications/filters',
    filteredDuplications: '/api/staff/duplications/filtered',
    dupContent: '/staff/dup-content', // Match StaffViewController
    statContent: '/staff/stat-content',
    logContent: '/staff/log-content',
    dupDetails: '/staff/dup-details',
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

function bindTabEvents() {
    const tabs = document.querySelectorAll(".tab");
    if (tabs.length === 0) {
        console.warn("No tab elements found to bind events");
        return;
    }
    
    tabs.forEach((tab) => {
        if (tab && tab.addEventListener) {
            tab.addEventListener("click", () => handleTabClick(tab));
        }
    });
}

function handleTabClick(tab) {
    if (!tab) {
        console.error("Tab element is null");
        return;
    }
    
    const tabs = document.querySelectorAll(".tab");
    if (tabs.length === 0) {
        console.error("No tab elements found");
        return;
    }
    
    tabs.forEach((t) => {
        if (t && t.classList) {
            t.classList.remove("active");
        }
    });
    
    if (tab.classList) {
        tab.classList.add("active");
    }

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
                
                // Reload data for the active tab after content is loaded
                if (tabName === 'detection') {
                    loadDuplications();
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
            // Clear existing options except "All Subjects"
            subjectSelect.innerHTML = '<option value="">All Subjects</option>';
            
            data.subjects.forEach(subject => {
                const option = document.createElement('option');
                option.value = subject;
                option.textContent = subject;
                subjectSelect.appendChild(option);
            });
        }
        
        // Populate submitter filter
        const submitterSelect = document.querySelector('.filter-select.submitter');
        if (submitterSelect && data.submitters) {
            // Clear existing options except "All Submitters"
            submitterSelect.innerHTML = '<option value="">All Submitters</option>';
            
            data.submitters.forEach(submitter => {
                const option = document.createElement('option');
                option.value = submitter;
                option.textContent = submitter;
                submitterSelect.appendChild(option);
            });
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
        subjectSelect.addEventListener('change', applyFilters);
    }
    
    if (submitterSelect) {
        submitterSelect.addEventListener('change', applyFilters);
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
            
            return `
            <tr>
                <td class="question-cell">${d.newQuestion?.content || 'N/A'}</td>
                <td class="question-cell">${d.similarQuestion?.content || 'N/A'}</td>
                <td>
                    <div class="${d.similarityScore >= 0.9 ? 'similarity-complete' : 'similarity-high'} similarity-badge">
                        ${d.similarityScore >= 0.9 ? 'Complete Duplicate' : 'High Similarity'} (${(d.similarityScore * 100).toFixed(1)}%)
                    </div>
                </td>
                <td><div class="subject-badge">${d.newQuestion?.courseName || 'N/A'}</div></td>
                <td class="submitter-name">${d.newQuestion?.creatorName || 'N/A'}</td>
                <td class="action-buttons">
                    <button class="btn-view" data-detection-id="${d.detectionId}">View</button>
                </td>
            </tr>
        `;
        }).join('');
        console.log('Data displayed successfully in table');
        
        // Bind filter events after content is loaded
        setTimeout(() => {
            bindFilterEvents();
        }, 100);
        
    } else {
        console.error('Table tbody not found! Content may not be loaded yet.');
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

function bindInnerEvents() {
    document.querySelectorAll(".filter-select").forEach((select) => {
        select.addEventListener("change", () => loadDuplications());
    });
}

function bindGlobalEvents() {
    document.addEventListener("click", function (e) {
        const target = e.target;
        if (target && target.classList && target.classList.contains("btn-view")) {
            e.preventDefault();
            handleViewDetail(target.dataset.detectionId);
        } else if (target && target.classList && target.classList.contains("back-link")) {
            e.preventDefault();
            const comparisonContainer = document.getElementById("comparison-container");
            const tabContent = document.getElementById("tab-content");
            if (comparisonContainer) comparisonContainer.style.display = "none";
            if (tabContent) tabContent.style.display = "block";
        }
    });
}

function handleViewDetail(detectionId) {
    const tabContent = document.getElementById("tab-content");
    const comparisonContainer = document.getElementById("comparison-container");

    tabContent.style.display = "none";

    fetch(`${API_URLS.dupDetails}?detectionId=${detectionId}`, {
        headers: { 'Accept': 'text/html' }
    })
        .then((res) => {
            if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
            return res.text();
        })
        .then((html) => {
            comparisonContainer.innerHTML = html;
            comparisonContainer.style.display = "block";
            loadCSS("/css/staff/staffDupDetails.css");
        })
        .catch((err) => {
            comparisonContainer.innerHTML = "<p>Error loading details: " + err.message + "</p>";
            console.error("Detail load error:", err);
        });
}

function closeModal() {
    document.getElementById("logDetailModal").style.display = "none";
}

document.addEventListener("DOMContentLoaded", () => {
    bindTabEvents();
    bindGlobalEvents();
    loadFilterOptions();
    loadDuplications();
    
    // Delay tab initialization to ensure DOM is fully ready
    setTimeout(() => {
        const firstTab = document.querySelector(".tab.active") || document.querySelector(".tab");
        if (firstTab) {
            firstTab.click();
        }
    }, 100);
});