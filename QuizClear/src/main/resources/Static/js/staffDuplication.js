// Load CSS động chỉ một lần
function loadCSS(href) {
  let link = document.querySelector("link[data-tab-css]");
  if (link) {
    link.href = href;
  } else {
    link = document.createElement("link");
    link.rel = "stylesheet";
    link.href = href;
    link.setAttribute("data-tab-css", "true");
    document.head.appendChild(link);
  }
}

let currentFilters = {
  subject: "",
  submitter: ""
};

// =================== API ENDPOINTS ===================
const API_BASE_URL = "http://localhost:8081/api/duplications";

// =================== FILTER FUNCTIONS ===================
async function loadFilterOptions() {
  try {
    const res = await fetch(`${API_BASE_URL}/filters`);
    if (!res.ok) {
      throw new Error(`HTTP error! status: ${res.status}`);
    }
    const data = await res.json();

    const subjectSelect = document.querySelectorAll(".filter-select")[0];
    const submitterSelect = document.querySelectorAll(".filter-select")[1];

    if (subjectSelect) {
      subjectSelect.innerHTML = '<option value="">All Subjects</option>';
      data.subjects.forEach((subj) => {
        const opt = document.createElement("option");
        opt.value = subj;
        opt.textContent = subj;
        subjectSelect.appendChild(opt);
      });
    }

    if (submitterSelect) {
      submitterSelect.innerHTML = '<option value="">All Submitters</option>';
      data.submitters.forEach((user) => {
        const opt = document.createElement("option");
        opt.value = user;
        opt.textContent = user;
        submitterSelect.appendChild(opt);
      });
    }
  } catch (err) {
    console.error("Error loading filter options:", err);
    showErrorMessage("Failed to load filter options");
  }
}

// =================== DUPLICATE DETECTION FUNCTIONS ===================
async function loadDuplications(filters = currentFilters) {
  try {
    const query = new URLSearchParams(filters).toString();
    const res = await fetch(`${API_BASE_URL}?${query}`);
    if (!res.ok) {
      throw new Error(`HTTP error! status: ${res.status}`);
    }
    const data = await res.json();
    
    const tableBody = document.querySelector(".detection-section tbody");
    if (!tableBody) {
      console.error("Table body not found");
      return;
    }
    
    tableBody.innerHTML = "";

    if (data.length === 0) {
      tableBody.innerHTML = '<tr><td colspan="6">No duplicate questions found.</td></tr>';
      return;
    }

    data.forEach((dup) => {
      const row = document.createElement("tr");
      row.dataset.id = dup.detectionId;
      
      // Format similarity score
      const similarityScore = (dup.similarityScore * 100).toFixed(2);
      const priorityClass = getPriorityClass(dup.similarityScore);
      
      row.innerHTML = `
        <td>
          <span class="question-text" title="${escapeHtml(dup.newQuestion?.content || 'N/A')}">
            ${truncateText(dup.newQuestion?.content || 'N/A', 50)}
          </span>
        </td>
        <td>
          <span class="question-text" title="${escapeHtml(dup.similarQuestion?.content || 'N/A')}">
            ${truncateText(dup.similarQuestion?.content || 'N/A', 50)}
          </span>
        </td>
        <td>
          <span class="similarity-score ${priorityClass}">
            ${similarityScore}%
          </span>
        </td>
        <td>${dup.newQuestion?.subject || 'Unknown'}</td>
        <td>${dup.detectedBy?.fullName || 'System'}</td>
        <td class="actions">
          <button class="btn btn-view" data-id="${dup.detectionId}">
            <i class="fas fa-eye"></i> View
          </button>
          ${dup.status === 'PENDING' ? 
            `<button class="btn btn-process" data-id="${dup.detectionId}">
              <i class="fas fa-cog"></i> Process
            </button>` : 
            `<span class="status-badge status-${dup.status.toLowerCase()}">${dup.status}</span>`
          }
        </td>
      `;
      tableBody.appendChild(row);
    });

    // Add event listeners
    document.querySelectorAll(".btn-view").forEach((button) => {
      button.addEventListener("click", (e) => {
        e.preventDefault();
        showDetail(e.target.closest('.btn-view').dataset.id);
      });
    });

    document.querySelectorAll(".btn-process").forEach((button) => {
      button.addEventListener("click", (e) => {
        e.preventDefault();
        showProcessModal(e.target.closest('.btn-process').dataset.id);
      });
    });

    // Update statistics
    updateDashboardStats();
    
  } catch (err) {
    console.error("Error loading duplications:", err);
    const tableBody = document.querySelector(".detection-section tbody");
    if (tableBody) {
      tableBody.innerHTML = '<tr><td colspan="6">Error loading data. Please try again.</td></tr>';
    }
    showErrorMessage("Failed to load duplicate detections");
  }
}

async function showDetail(id) {
  try {
    const tabContent = document.getElementById("tab-content");
    const comparisonContainer = document.getElementById("comparison-container");

    tabContent.style.display = "none";
    comparisonContainer.style.display = "block";

    const htmlRes = await fetch("http://localhost:8081/staff/duplication-details");
    const htmlContent = await htmlRes.text();
    comparisonContainer.innerHTML = htmlContent;

    const dataRes = await fetch(`http://localhost:8081/api/duplications/${id}`);
    const data = await dataRes.json();

    document.querySelector(".question-card #new-question-text").textContent = data.new_question_text;
    document.getElementById("new-question-subject").textContent = data.new_question_subject;
    document.getElementById("new-question-clo").textContent = data.new_question_clo;
    document.getElementById("new-question-difficulty").textContent = data.new_question_difficulty;
    document.getElementById("new-question-creator").textContent = data.new_question_creator;
    document.getElementById("new-question-created-at").textContent = new Date(data.new_question_created_at).toLocaleString();
    document.querySelector(".question-card #new-question-options").innerHTML = data.new_question_options;

    document.querySelector(".question-card #similar-question-text").textContent = data.similar_question_text;
    document.getElementById("similar-question-subject").textContent = data.similar_question_subject;
    document.getElementById("similar-question-clo").textContent = data.similar_question_clo;
    document.getElementById("similar-question-difficulty").textContent = data.similar_question_difficulty;
    document.getElementById("similar-question-creator").textContent = data.similar_question_creator;
    document.getElementById("similar-question-created-at").textContent = new Date(data.similar_question_created_at).toLocaleString();
    document.querySelector(".question-card #similar-question-options").innerHTML = data.similar_question_options;

    const backLink = document.querySelector(".back-link");
    backLink.addEventListener("click", (e) => {
      e.preventDefault();
      comparisonContainer.style.display = "none";
      tabContent.style.display = "block";
    });

    const submitActionBtn = document.getElementById("submit-action");
    submitActionBtn.addEventListener("click", async () => {
      const action = document.querySelector("input[name='action']:checked").id;
      const feedback = document.getElementById("feedback").value;
      const processedBy = 1;

      await fetch(`http://localhost:8081/api/duplications/${id}/process`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ action, feedback, processedBy }),
      });

      alert("Processed successfully!");
      comparisonContainer.style.display = "none";
      tabContent.style.display = "block";
      await loadDuplications();
    });
  } catch (err) {
    comparisonContainer.innerHTML = "<p>Error loading detail.</p>";
    console.error("Detail load error:", err);
  }
}

// =================== HELPER FUNCTIONS ===================
function escapeHtml(text) {
  const div = document.createElement('div');
  div.textContent = text;
  return div.innerHTML;
}

function truncateText(text, maxLength) {
  if (!text) return 'N/A';
  return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
}

function getPriorityClass(score) {
  if (score >= 0.8) return 'high-priority';
  if (score >= 0.6) return 'medium-priority';
  return 'low-priority';
}

function showErrorMessage(message) {
  // Create toast notification
  const toast = document.createElement('div');
  toast.className = 'toast error-toast';
  toast.innerHTML = `
    <i class="fas fa-exclamation-circle"></i>
    <span>${message}</span>
  `;
  document.body.appendChild(toast);
  
  setTimeout(() => {
    toast.remove();
  }, 5000);
}

function showSuccessMessage(message) {
  const toast = document.createElement('div');
  toast.className = 'toast success-toast';
  toast.innerHTML = `
    <i class="fas fa-check-circle"></i>
    <span>${message}</span>
  `;
  document.body.appendChild(toast);
  
  setTimeout(() => {
    toast.remove();
  }, 3000);
}

// =================== AI DUPLICATE CHECK FUNCTIONS ===================
async function triggerAiCheck() {
  try {
    showLoadingState(true);
    const response = await fetch(`${API_BASE_URL}/ai-check/trigger`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const result = await response.json();
    showSuccessMessage(`AI check completed! Found ${result.duplicatesFound} potential duplicates.`);
    
    // Refresh the duplications list
    await loadDuplications();
    
  } catch (error) {
    console.error('Error triggering AI check:', error);
    showErrorMessage('Failed to trigger AI duplicate check. Please try again.');
  } finally {
    showLoadingState(false);
  }
}

async function getAiServiceHealth() {
  try {
    const response = await fetch(`${API_BASE_URL}/ai-service/health`);
    const health = await response.json();
    
    const statusElement = document.getElementById('ai-service-status');
    if (statusElement) {
      statusElement.innerHTML = `
        <span class="status-indicator ${health.status === 'UP' ? 'status-up' : 'status-down'}"></span>
        AI Service: ${health.status}
        ${health.responseTime ? `(${health.responseTime}ms)` : ''}
      `;
    }
    
    return health;
  } catch (error) {
    console.error('Error checking AI service health:', error);
    const statusElement = document.getElementById('ai-service-status');
    if (statusElement) {
      statusElement.innerHTML = `
        <span class="status-indicator status-down"></span>
        AI Service: OFFLINE
      `;
    }
    return { status: 'DOWN' };
  }
}

function showLoadingState(isLoading) {
  const loadingElements = document.querySelectorAll('.loading-indicator');
  const actionButtons = document.querySelectorAll('.btn-primary, .btn-process');
  
  if (isLoading) {
    loadingElements.forEach(el => el.style.display = 'block');
    actionButtons.forEach(btn => btn.disabled = true);
  } else {
    loadingElements.forEach(el => el.style.display = 'none');
    actionButtons.forEach(btn => btn.disabled = false);
  }
}

// =================== STATISTICS FUNCTIONS ===================
async function updateDashboardStats() {
  try {
    const response = await fetch(`${API_BASE_URL}/statistics`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const stats = await response.json();
    
    // Update stat cards if they exist
    const totalDetections = document.getElementById('total-detections');
    const pendingReviews = document.getElementById('pending-reviews');
    const highPriority = document.getElementById('high-priority');
    const avgSimilarity = document.getElementById('avg-similarity');
    
    if (totalDetections) totalDetections.textContent = stats.totalDetections || 0;
    if (pendingReviews) pendingReviews.textContent = stats.pendingReviews || 0;
    if (highPriority) highPriority.textContent = stats.highPriorityCount || 0;
    if (avgSimilarity) avgSimilarity.textContent = `${(stats.averageSimilarity * 100).toFixed(1)}%`;
    
  } catch (error) {
    console.error('Error updating dashboard stats:', error);
  }
}

// =================== PROCESSING FUNCTIONS ===================
async function showProcessModal(detectionId) {
  try {
    // Create modal HTML
    const modalHtml = `
      <div id="process-modal" class="modal">
        <div class="modal-content">
          <span class="close">&times;</span>
          <h2>Process Duplicate Detection</h2>
          <div id="modal-loading" class="loading-indicator">Loading...</div>
          <div id="modal-content" style="display: none;">
            <div class="action-selection">
              <h3>Action Required</h3>
              <label>
                <input type="radio" name="action" value="APPROVE_DUPLICATE" id="approve-duplicate">
                Mark as Duplicate (Remove new question)
              </label>
              <label>
                <input type="radio" name="action" value="REJECT_DUPLICATE" id="reject-duplicate">
                Not a Duplicate (Keep both questions)
              </label>
              <label>
                <input type="radio" name="action" value="NEEDS_REVIEW" id="needs-review">
                Needs Further Review
              </label>
            </div>
            <div class="feedback-section">
              <h3>Comments (Optional)</h3>
              <textarea id="process-feedback" placeholder="Add your comments here..."></textarea>
            </div>
            <div class="modal-actions">
              <button class="btn btn-primary" id="submit-process">Submit Decision</button>
              <button class="btn btn-secondary" id="cancel-process">Cancel</button>
            </div>
          </div>
        </div>
      </div>
    `;
    
    // Add modal to page
    document.body.insertAdjacentHTML('beforeend', modalHtml);
    
    // Load detection details
    const response = await fetch(`${API_BASE_URL}/${detectionId}`);
    const detection = await response.json();
    
    // Hide loading, show content
    document.getElementById('modal-loading').style.display = 'none';
    document.getElementById('modal-content').style.display = 'block';
    
    // Add event listeners
    document.getElementById('submit-process').addEventListener('click', () => {
      processDetection(detectionId);
    });
    
    document.getElementById('cancel-process').addEventListener('click', closeProcessModal);
    document.querySelector('.close').addEventListener('click', closeProcessModal);
    
    // Show modal
    document.getElementById('process-modal').style.display = 'block';
    
  } catch (error) {
    console.error('Error showing process modal:', error);
    showErrorMessage('Failed to load detection details');
  }
}

function closeProcessModal() {
  const modal = document.getElementById('process-modal');
  if (modal) {
    modal.remove();
  }
}

async function processDetection(detectionId) {
  try {
    const actionElement = document.querySelector('input[name="action"]:checked');
    if (!actionElement) {
      showErrorMessage('Please select an action');
      return;
    }
    
    const action = actionElement.value;
    const feedback = document.getElementById('process-feedback').value;
    
    const response = await fetch(`${API_BASE_URL}/${detectionId}/process`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        action: action,
        feedback: feedback,
        processedBy: 1 // TODO: Get from current user session
      })
    });
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    showSuccessMessage('Detection processed successfully!');
    closeProcessModal();
    await loadDuplications(); // Refresh the list
    
  } catch (error) {
    console.error('Error processing detection:', error);
    showErrorMessage('Failed to process detection. Please try again.');
  }
}

// =================== FILTER FUNCTIONS ENHANCEMENT ===================
function applyFilters() {
  const subjectFilter = document.getElementById('subject-filter')?.value || '';
  const submitterFilter = document.getElementById('submitter-filter')?.value || '';
  const statusFilter = document.getElementById('status-filter')?.value || '';
  const priorityFilter = document.getElementById('priority-filter')?.value || '';
  
  currentFilters = {
    subject: subjectFilter,
    submitter: submitterFilter,
    status: statusFilter,
    priority: priorityFilter
  };
  
  loadDuplications(currentFilters);
}

function clearFilters() {
  // Reset all filter inputs
  const filterInputs = document.querySelectorAll('.filter-select, .filter-input');
  filterInputs.forEach(input => {
    input.value = '';
  });
  
  currentFilters = {
    subject: '',
    submitter: '',
    status: '',
    priority: ''
  };
  
  loadDuplications();
}

// =================== PROCESSING LOGS ===================
async function loadProcessingLogs() {
  try {
    const response = await fetch(`${API_BASE_URL}/processing-logs`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const logs = await response.json();
    
    const tableBody = document.querySelector('.logs-section tbody');
    if (!tableBody) return;
    
    tableBody.innerHTML = '';
    
    if (logs.length === 0) {
      tableBody.innerHTML = '<tr><td colspan="6">No processing logs found.</td></tr>';
      return;
    }
    
    logs.forEach(log => {
      const row = document.createElement('tr');
      row.innerHTML = `
        <td>${log.detectionId}</td>
        <td>${log.action}</td>
        <td>${log.processedBy}</td>
        <td>${new Date(log.processedAt).toLocaleString()}</td>
        <td>${log.feedback || 'No comments'}</td>
        <td>
          <button class="btn btn-view-small" onclick="showLogDetail(${log.id})">
            <i class="fas fa-eye"></i>
          </button>
        </td>
      `;
      tableBody.appendChild(row);
    });
    
  } catch (error) {
    console.error('Error loading processing logs:', error);
    showErrorMessage('Failed to load processing logs');
  }
}

// =================== MAIN INITIALIZATION ===================
document.addEventListener("DOMContentLoaded", async () => {
  // Initialize page
  await loadFilterOptions();
  await loadDuplications();
  await getAiServiceHealth();
  
  // Set up periodic health checks
  setInterval(getAiServiceHealth, 30000); // Check every 30 seconds
  
  // Set up filter event listeners
  const filterElements = document.querySelectorAll('.filter-select, .filter-input');
  filterElements.forEach(element => {
    element.addEventListener('change', applyFilters);
  });
  
  // Set up AI check button
  const aiCheckButton = document.getElementById('trigger-ai-check');
  if (aiCheckButton) {
    aiCheckButton.addEventListener('click', triggerAiCheck);
  }
  
  // Set up clear filters button
  const clearFiltersButton = document.getElementById('clear-filters');
  if (clearFiltersButton) {
    clearFiltersButton.addEventListener('click', clearFilters);
  }

  // Handle tab functionality if present
  document.querySelectorAll(".tab").forEach((tab) => {
    tab.addEventListener("click", async (e) => {
      document.querySelectorAll(".tab").forEach((t) => t.classList.remove("active"));
      e.target.classList.add("active");

      const tabId = e.target.dataset.tab;
      const tabContentDiv = document.getElementById("tab-content");
      let htmlUrl = "";
      if (tabId === "detection") {
        htmlUrl = "/staff/duplication-content";
        loadCSS("/css/staff/staffDup.css");
      } else if (tabId === "stat") {
        htmlUrl = "/staff/stat-content";
        loadCSS("/css/staff/staffStatRp.css");
      } else if (tabId === "proc_log") {
        htmlUrl = "/staff/log-content";
        loadCSS("/css/staff/staffLogsDetails.css");
      }

      if (htmlUrl) {
        try {
          const res = await fetch(`http://localhost:8081${htmlUrl}`);
          const html = await res.text();
          tabContentDiv.innerHTML = html;

          if (tabId === "detection") {
            await loadFilterOptions();
            await loadDuplications();
          } else if (tabId === "proc_log") {
            await loadProcessingLogs();
          }
        } catch (err) {
          console.error("Error loading tab content:", err);
          tabContentDiv.innerHTML = "<p>Error loading content.</p>";
        }
      }
    });
  });

  // Load initial tab content if tab system is present
  const tabContentDiv = document.getElementById("tab-content");
  if (tabContentDiv) {
    try {
      const res = await fetch("http://localhost:8081/staff/duplication-content");
      const html = await res.text();
      tabContentDiv.innerHTML = html;
    } catch (err) {
      console.error("Error loading initial tab content:", err);
      tabContentDiv.innerHTML = "<p>Error loading initial content.</p>";
    }
  }
});
