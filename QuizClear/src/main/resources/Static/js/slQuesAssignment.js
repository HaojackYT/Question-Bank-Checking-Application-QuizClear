document.addEventListener('DOMContentLoaded', function() {
    // Dữ liệu mẫu (thường sẽ được lấy từ API)
    let assignmentsData = [
        {
            id: 1,
            lecturer: "Nguyen Van A",
            department: "IT",
            subject: "Database",
            totalQues: 20,
            diffLevels: "Reg",
            clos: "CLO1, CLO2",
            assignDate: "2024-04-25",
            dueDate: "2024-04-25",
            progress: "100%",
            status: "Completed",
            notes: "Bài tập đã được hoàn thành đúng hạn."
        },
        {
            id: 2,
            lecturer: "Nguyen Van B",
            department: "Business",
            subject: "Web Development",
            totalQues: 40,
            diffLevels: "Comp",
            clos: "CLO1",
            assignDate: "2024-04-25",
            dueDate: "2024-05-10",
            progress: "50%",
            status: "In Prog.",
            notes: "Cần xem xét lại các câu hỏi khó."
        },
        {
            id: 3,
            lecturer: "Nguyen Van B",
            department: "IT",
            subject: "Basic Application",
            totalQues: 50,
            diffLevels: "Bas App",
            clos: "CLO2",
            assignDate: "2024-04-20",
            dueDate: "2024-05-05",
            progress: "0%",
            status: "Not started",
            notes: "Giảng viên chưa bắt đầu."
        }
    ];

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
    function renderAssignmentsTable() {
        const tableBody = document.querySelector('.assignment-table tbody');
        if (!tableBody) return;

        tableBody.innerHTML = ''; // Xóa nội dung hiện có

        assignmentsData.forEach(assignment => {
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


    // Khởi tạo bảng khi tải trang lần đầu
    renderAssignmentsTable();
});