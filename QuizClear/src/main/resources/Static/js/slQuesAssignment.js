document.addEventListener('DOMContentLoaded', function() {
    // Global scope variables for user context
    let currentUserScope = {
        userId: null,
        userRole: null,
        accessibleDepartmentIds: [],
        accessibleSubjectIds: [],
        canAssignToAll: false
    };

    // Initialize user scope from backend
    initializeUserScope();

    // Enhanced assignments data structure for scope filtering
    let assignmentsData = [];

    // Lấy các phần tử Modal và Nút đóng chung
    const modals = document.querySelectorAll('.modal');
    const closeButtons = document.querySelectorAll('.modal .close-button');

    // Nút New Assign
    const newAssignBtn = document.getElementById('newAssignBtn');
    const newAssignModal = document.getElementById('newAssignModal');
    const newAssignForm = document.getElementById('newAssignForm');
    const newAssignCancelBtn = newAssignModal.querySelector('.cancel-button');

    // Modal View Details
    const viewDetailsModal = document.getElementById('viewDetailsModal');
    const viewDetailsTitle = document.getElementById('viewDetailsTitle');
    const viewLecturer = document.getElementById('viewLecturer');
    const viewDepartment = document.getElementById('viewDepartment');
    const viewSubject = document.getElementById('viewSubject');
    const viewTotalQues = document.getElementById('viewTotalQues');
    const viewDiffLevels = document.getElementById('viewDiffLevels');
    const viewCLOs = document.getElementById('viewCLOs');
    const viewAssignDate = document.getElementById('viewAssignDate');
    const viewDueDate = document.getElementById('viewDueDate');
    const viewProgress = document.getElementById('viewProgress');
    const viewStatus = document.getElementById('viewStatus');


    // Modal Edit Assignment
    const editAssignmentModal = document.getElementById('editAssignmentModal');
    const editAssignmentTitle = document.getElementById('editAssignmentTitle');
    const editAssignmentForm = document.getElementById('editAssignmentForm');
    const editAssignmentId = document.getElementById('editAssignmentId');
    const editLecturer = document.getElementById('editLecturer');
    const editDepartment = document.getElementById('editDepartment');
    const editSubject = document.getElementById('editSubject');
    const editTotalQues = document.getElementById('editTotalQues');
    const editDiffLevels = document.getElementById('editDiffLevels');
    const editCLOs = document.getElementById('editCLOs');
    const editAssignDate = document.getElementById('editAssignDate');
    const editDueDate = document.getElementById('editDueDate');
    const editStatus = document.getElementById('editStatus');
    const editNotes = document.getElementById('editNotes');
    const editDeleteBtn = editAssignmentModal.querySelector('.delete-button');
    const editSaveBtn = editAssignmentModal.querySelector('.save-button');


    // Hàm chung để mở modal
function openModal(modalElement) {
        // THAY ĐỔI: Thêm class 'show' để hiển thị modal
        modalElement.classList.add('show');
    }

    // Hàm chung để đóng modal
    function closeModal(modalElement) {
        // THAY ĐỔI: Xóa class 'show' để ẩn modal
        modalElement.classList.remove('show');
        // Reset form nếu là modal có form
        if (modalElement.querySelector('form')) {
            modalElement.querySelector('form').reset();
        }
    }

    // Khởi tạo bảng khi tải trang lần đầu (để đảm bảo nút View/Edit có sẵn)
    renderAssignmentsTable();

    // Gán sự kiện cho các nút đóng chung (x)
    closeButtons.forEach(button => {
        button.addEventListener('click', function() {
            const modalElement = this.closest('.modal'); // Tìm modal cha gần nhất
            closeModal(modalElement);
        });
    });

    // Đóng modal khi click ra bên ngoài nội dung modal (chung cho tất cả modal)
    modals.forEach(modal => {
        modal.addEventListener('click', function(event) {
            // Đảm bảo chỉ đóng modal khi click trực tiếp vào phần nền (không phải nội dung modal-content)
            if (event.target === modal) {
                closeModal(modal);
            }
        });
    });

    // --- Logic cho New Assign Modal ---
    if (newAssignBtn) {
        newAssignBtn.addEventListener('click', function() {
            openModal(newAssignModal);
            // Đặt giá trị mặc định cho Assign Date và Due Date khi mở modal mới
            const today = new Date();
            const tomorrow = new Date(today);
            tomorrow.setDate(today.getDate() + 7); // Mặc định 7 ngày sau

            document.getElementById('assignDate').value = today.toISOString().split('T')[0];
            document.getElementById('dueDate').value = tomorrow.toISOString().split('T')[0];
        });
    }

    if (newAssignCancelBtn) {
        newAssignCancelBtn.addEventListener('click', function() {
            closeModal(newAssignModal);
        });
    }

    if (newAssignForm) {
        newAssignForm.addEventListener('submit', function(event) {
            event.preventDefault();

            // Lấy dữ liệu từ form
            const newAssignment = {
                id: assignmentsData.length > 0 ? Math.max(...assignmentsData.map(a => a.id)) + 1 : 1, // ID tăng dần
                lecturer: document.getElementById('lecturer').value,
                department: document.getElementById('department').value,
                subject: document.getElementById('subject').value,
                totalQues: parseInt(document.getElementById('totalQues').value),
                diffLevels: document.getElementById('diffLevels').value,
                clos: document.getElementById('clos').value,
                assignDate: document.getElementById('assignDate').value,
                dueDate: document.getElementById('dueDate').value,
                progress: "0%", // Mặc định khi tạo mới
                status: "Not started", // Mặc định khi tạo mới
                notes: document.getElementById('notes').value
            };

            // Thêm vào dữ liệu mẫu (trong thực tế sẽ gửi lên server)
            assignmentsData.push(newAssignment);
            console.log('New Assignment Added:', newAssignment);
            alert('Phân công mới đã được tạo thành công!');

            // Cập nhật lại bảng
            renderAssignmentsTable();

            closeModal(newAssignModal);
        });
    }

    if (editDeleteBtn) {
        editDeleteBtn.addEventListener('click', function() {
            const assignmentId = parseInt(editAssignmentId.value);
            if (confirm(`Bạn có chắc chắn muốn xóa phân công số ${assignmentId} này không?`)) {
                // Xóa khỏi mảng dữ liệu
                const initialLength = assignmentsData.length;
                assignmentsData = assignmentsData.filter(a => a.id !== assignmentId); // Dùng filter để tạo mảng mới
                if (assignmentsData.length < initialLength) {
                    alert(`Phân công số ${assignmentId} đã được xóa.`);
                    renderAssignmentsTable(); // Cập nhật lại bảng
                    closeModal(editAssignmentModal);
                } else {
                    alert('Không tìm thấy phân công để xóa.');
                }
            }
        });
    }

    if (editAssignmentForm) {
        editAssignmentForm.addEventListener('submit', function(event) {
            event.preventDefault();

            const assignmentId = parseInt(editAssignmentId.value);
            const assignmentIndex = assignmentsData.findIndex(a => a.id === assignmentId);

            if (assignmentIndex > -1) {
                // Cập nhật dữ liệu trong mảng
                assignmentsData[assignmentIndex].lecturer = editLecturer.value;
                assignmentsData[assignmentIndex].department = editDepartment.value;
                assignmentsData[assignmentIndex].subject = editSubject.value;
                assignmentsData[assignmentIndex].totalQues = parseInt(editTotalQues.value);
                assignmentsData[assignmentIndex].diffLevels = editDiffLevels.value;
                assignmentsData[assignmentIndex].clos = editCLOs.value;
                assignmentsData[assignmentIndex].assignDate = editAssignDate.value;
                assignmentsData[assignmentIndex].dueDate = editDueDate.value;
                assignmentsData[assignmentIndex].status = editStatus.value;
                assignmentsData[assignmentIndex].notes = editNotes.value;

                console.log('Assignment Updated:', assignmentsData[assignmentIndex]);
                alert(`Phân công số ${assignmentId} đã được cập nhật.`);
                renderAssignmentsTable(); // Cập nhật lại bảng
                closeModal(editAssignmentModal);
            } else {
                alert('Không tìm thấy phân công để cập nhật.');
            }
        });
    }

    // --- Hàm render bảng để cập nhật động dữ liệu ---
    function renderAssignmentsTable(filteredData = assignmentsData) {
        const tableBody = document.querySelector('.assignment-table tbody');
        if (!tableBody) return;

        tableBody.innerHTML = ''; // Xóa nội dung hiện có

        filteredData.forEach(assignment => {
            const row = document.createElement('tr');
            row.dataset.id = assignment.id;

            let statusClass = '';
            if (assignment.status === 'Completed') {
                statusClass = 'completed';
            } else if (assignment.status === 'In Prog.') {
                statusClass = 'in-progress';
            } else if (assignment.status === 'Not started') {
                statusClass = 'not-started';
            }

            let actionsHtml = `
                <div class="actions">
                    <button class="action-button view-button" data-id="${assignment.id}"><i class="fa fa-eye"></i></button>
            `;
            // Chỉ thêm nút Edit nếu trạng thái không phải "Completed"
            if (assignment.status !== 'Completed') {
                actionsHtml += `<button class="action-button edit-button" data-id="${assignment.id}"><i class="fa fa-pencil"></i></button>`;
            }
            actionsHtml += `</div>`;


            row.innerHTML = `
                <td>${assignment.id}</td>
                <td>${assignment.lecturer}</td>
                <td>${assignment.totalQues}</td>
                <td>${assignment.diffLevels}</td>
                <td>${assignment.dueDate}</td>
                <td class="status-cell">
                    <span class="status ${statusClass}">${assignment.status}</span>
                </td>
                <td class="actions-cell">
                    ${actionsHtml}
                </td>
            `;
            tableBody.appendChild(row);
        });

        // Sau khi render lại bảng, cần gán lại sự kiện cho các nút view/edit mới
        attachActionButtonListeners();
    }

    // Hàm gán lại listeners cho các nút View/Edit
    function attachActionButtonListeners() {
        // Lấy tất cả các nút View và Edit mới
        const currentViewButtons = document.querySelectorAll('.view-button');
        const currentEditButtons = document.querySelectorAll('.edit-button');

        // Gán lại cho View buttons
        currentViewButtons.forEach(button => {
            // Đảm bảo không gắn nhiều lần bằng cách xóa listener cũ nếu có
            button.removeEventListener('click', handleViewButtonClick);
            button.addEventListener('click', handleViewButtonClick);
        });

        // Gán lại cho Edit buttons
        currentEditButtons.forEach(button => {
            // Đảm bảo không gắn nhiều lần bằng cách xóa listener cũ nếu có
            button.removeEventListener('click', handleEditButtonClick);
            button.addEventListener('click', handleEditButtonClick);
        });
    }

    // Hàm xử lý khi nút View được click
    function handleViewButtonClick() {
        const assignmentId = parseInt(this.dataset.id);
        const assignment = assignmentsData.find(a => a.id === assignmentId);

        if (assignment) {
            viewDetailsTitle.textContent = `Details No. ${assignment.id}`;
            viewLecturer.value = assignment.lecturer;
            viewDepartment.value = assignment.department;
            viewSubject.value = assignment.subject;
            viewTotalQues.value = assignment.totalQues;
            viewDiffLevels.value = assignment.diffLevels;
            viewCLOs.value = assignment.clos;
            viewAssignDate.value = assignment.assignDate;
            viewDueDate.value = assignment.dueDate;
            viewProgress.value = assignment.progress;
            viewStatus.value = assignment.status;

            openModal(viewDetailsModal);
        }
    }

    // Hàm xử lý khi nút Edit được click
    function handleEditButtonClick() {
        const assignmentId = parseInt(this.dataset.id);
        const assignment = assignmentsData.find(a => a.id === assignmentId);

        if (assignment) {
            editAssignmentTitle.textContent = `Edit No. ${assignment.id}`;
            editAssignmentId.value = assignment.id;
            editLecturer.value = assignment.lecturer;
            editDepartment.value = assignment.department;
            editSubject.value = assignment.subject;
            editTotalQues.value = assignment.totalQues;
            editDiffLevels.value = assignment.diffLevels;
            editCLOs.value = assignment.clos;
            editAssignDate.value = assignment.assignDate;
            editDueDate.value = assignment.dueDate;
            editStatus.value = assignment.status;
            editNotes.value = assignment.notes;

            openModal(editAssignmentModal);
        }
    }


    // Initialize user scope and load assignments
    async function initializeUserScope() {
        try {
            const response = await fetch('/api/user/current-scope', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            
            if (response.ok) {
                currentUserScope = await response.json();
                console.log('User scope initialized:', currentUserScope);
                await loadAssignments();
                await loadLecturersAndSubjects();
            } else {
                console.error('Failed to load user scope');
                // Fallback to load all data without filtering
                await loadAssignments();
            }
        } catch (error) {
            console.error('Error initializing user scope:', error);
            await loadAssignments();
        }
    }

    // Enhanced load assignments with scope filtering
    async function loadAssignments() {
        try {
            const params = new URLSearchParams();
            
            // Add scope filtering parameters
            if (currentUserScope.accessibleDepartmentIds.length > 0) {
                params.append('departmentIds', currentUserScope.accessibleDepartmentIds.join(','));
            }
            if (currentUserScope.accessibleSubjectIds.length > 0) {
                params.append('subjectIds', currentUserScope.accessibleSubjectIds.join(','));
            }
            params.append('requestingUserId', currentUserScope.userId);
            params.append('userRole', currentUserScope.userRole);

            const response = await fetch(`/api/assignments/question-assignments?${params}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                assignmentsData = await response.json();
                renderAssignmentsTable();
            } else {
                console.error('Failed to load assignments');
                // Use fallback sample data if API fails
                loadSampleData();
            }
        } catch (error) {
            console.error('Error loading assignments:', error);
            loadSampleData();
        }
    }

    // Load lecturers and subjects filtered by scope
    async function loadLecturersAndSubjects() {
        try {
            // Load lecturers within scope
            const lecturersResponse = await fetch(`/api/users/lecturers?departmentIds=${currentUserScope.accessibleDepartmentIds.join(',')}&subjectIds=${currentUserScope.accessibleSubjectIds.join(',')}&requestingUserId=${currentUserScope.userId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (lecturersResponse.ok) {
                const lecturers = await lecturersResponse.json();
                populateLecturerDropdowns(lecturers);
            }

            // Load subjects within scope
            const subjectsResponse = await fetch(`/api/subjects?departmentIds=${currentUserScope.accessibleDepartmentIds.join(',')}&requestingUserId=${currentUserScope.userId}&userRole=${currentUserScope.userRole}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (subjectsResponse.ok) {
                const subjects = await subjectsResponse.json();
                populateSubjectDropdowns(subjects);
            }
        } catch (error) {
            console.error('Error loading lecturers and subjects:', error);
        }
    }

    // Populate lecturer dropdowns with scope-filtered data
    function populateLecturerDropdowns(lecturers) {
        const lecturerSelects = document.querySelectorAll('select[name="lecturer"], #editLecturer, #newAssignLecturer');
        
        lecturerSelects.forEach(select => {
            // Clear existing options except the first one (placeholder)
            while (select.children.length > 1) {
                select.removeChild(select.lastChild);
            }

            lecturers.forEach(lecturer => {
                // Only show lecturers within scope
                if (isLecturerInScope(lecturer)) {
                    const option = document.createElement('option');
                    option.value = lecturer.userId;
                    option.textContent = `${lecturer.fullName} (${lecturer.departmentName})`;
                    option.dataset.departmentId = lecturer.departmentId;
                    option.dataset.subjectIds = lecturer.subjectIds ? lecturer.subjectIds.join(',') : '';
                    select.appendChild(option);
                }
            });
        });
    }

    // Populate subject dropdowns with scope-filtered data
    function populateSubjectDropdowns(subjects) {
        const subjectSelects = document.querySelectorAll('select[name="subject"], #editSubject, #newAssignSubject');
        
        subjectSelects.forEach(select => {
            // Clear existing options except the first one (placeholder)
            while (select.children.length > 1) {
                select.removeChild(select.lastChild);
            }

            subjects.forEach(subject => {
                // Only show subjects within scope
                if (isSubjectInScope(subject)) {
                    const option = document.createElement('option');
                    option.value = subject.subjectId;
                    option.textContent = `${subject.subjectName} (${subject.subjectCode}) - ${subject.departmentName}`;
                    option.dataset.departmentId = subject.departmentId;
                    option.dataset.credits = subject.credits;
                    select.appendChild(option);
                }
            });
        });
    }

    // Check if lecturer is within current user scope
    function isLecturerInScope(lecturer) {
        if (currentUserScope.canAssignToAll) return true;
        
        // Check department scope
        const hasDepartmentAccess = currentUserScope.accessibleDepartmentIds.length === 0 || 
                                   currentUserScope.accessibleDepartmentIds.includes(lecturer.departmentId);
        
        // Check subject scope
        const hasSubjectAccess = currentUserScope.accessibleSubjectIds.length === 0 ||
                                lecturer.subjectIds.some(subjectId => 
                                    currentUserScope.accessibleSubjectIds.includes(subjectId));
        
        return hasDepartmentAccess && hasSubjectAccess;
    }

    // Check if subject is within current user scope
    function isSubjectInScope(subject) {
        if (currentUserScope.canAssignToAll) return true;
        
        // Check department scope
        const hasDepartmentAccess = currentUserScope.accessibleDepartmentIds.length === 0 || 
                                   currentUserScope.accessibleDepartmentIds.includes(subject.departmentId);
        
        // Check subject scope
        const hasSubjectAccess = currentUserScope.accessibleSubjectIds.length === 0 ||
                                currentUserScope.accessibleSubjectIds.includes(subject.subjectId);
        
        return hasDepartmentAccess && hasSubjectAccess;
    }

    // Enhanced filter function with scope consideration
    function filterAssignments() {
        const searchTerm = document.getElementById('searchInput').value.toLowerCase();
        const departmentFilter = document.getElementById('departmentFilter').value;
        const statusFilter = document.getElementById('statusFilter').value;
        
        let filteredData = assignmentsData.filter(assignment => {
            // Basic filters
            const matchesSearch = assignment.lecturer.toLowerCase().includes(searchTerm) ||
                                assignment.subject.toLowerCase().includes(searchTerm);
            const matchesDepartment = !departmentFilter || assignment.departmentId == departmentFilter;
            const matchesStatus = !statusFilter || assignment.status === statusFilter;
            
            // Scope filtering - ensure assignment is within user's scope
            const withinScope = isAssignmentInScope(assignment);
            
            return matchesSearch && matchesDepartment && matchesStatus && withinScope;
        });
        
        renderAssignmentsTable(filteredData);
    }

    // Check if assignment is within current user scope
    function isAssignmentInScope(assignment) {
        if (currentUserScope.canAssignToAll) return true;
        
        const hasDepartmentAccess = currentUserScope.accessibleDepartmentIds.length === 0 || 
                                   currentUserScope.accessibleDepartmentIds.includes(assignment.departmentId);
        
        const hasSubjectAccess = currentUserScope.accessibleSubjectIds.length === 0 ||
                                currentUserScope.accessibleSubjectIds.includes(assignment.subjectId);
        
        return hasDepartmentAccess && hasSubjectAccess;
    }

    // Fallback sample data for development/testing
    function loadSampleData() {
        assignmentsData = [
            {
                id: 1,
                lecturer: "Nguyen Van A",
                lecturerId: 1,
                department: "IT",
                departmentId: 1,
                subject: "Database",
                subjectId: 1,
                totalQues: 20,
                diffLevels: "Reg",
                clos: "CLO1, CLO2",
                assignDate: "2024-04-25",
                dueDate: "2024-04-25",
                progress: "100%",
                status: "Completed",
                notes: "Bài tập đã được hoàn thành đúng hạn.",
                canEdit: true,
                canDelete: false
            },
            // Add more sample data as needed
        ];
        renderAssignmentsTable();
    }
});