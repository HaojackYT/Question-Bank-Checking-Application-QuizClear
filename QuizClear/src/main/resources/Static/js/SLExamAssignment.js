document.addEventListener('DOMContentLoaded', () => {
    // Re-use from New Assign modal, or ensure they are present
    const modalOverlay = document.getElementById('newAssignModalOverlay');
    const newAssignModal = document.getElementById('newAssignModal');
    const closeNewAssignButtons = document.querySelectorAll('.close-modal-btn');

    // New elements for Details modal
    const detailsModal = document.getElementById('detailsModal');
    const closeDetailsModalButtons = document.querySelectorAll('.close-details-modal-btn');
    const editButtons = document.querySelectorAll('.edit-icon');

    // Hàm để mở modal Details
    function openDetailsModal(examData) {
        document.getElementById('detailsExamTitle').value = examData.examTitle;
        document.getElementById('detailsLecturer').value = examData.lecturer;
        document.getElementById('detailsCourse').value = examData.course;
        document.getElementById('detailsNoOfExams').value = examData.noOfExams;
        document.getElementById('detailsStructure').value = examData.structure;
        document.getElementById('detailsAssignDate').value = examData.assignDate;
        document.getElementById('detailsDueDate').value = examData.dueDate;

        modalOverlay.style.display = 'block';
        detailsModal.style.display = 'block';
        document.body.classList.add('modal-open');
    }

    // Hàm để đóng modal Details
    function closeDetailsModal() {
        modalOverlay.style.display = 'none';
        detailsModal.style.display = 'none';
        document.body.classList.remove('modal-open');
    }

    // Lắng nghe sự kiện click cho các nút "cái bút" (Edit)
    editButtons.forEach(button => {
        button.addEventListener('click', (event) => {
            const row = event.target.closest('tr');
            if (row) {
                const examTitle = row.querySelector('td:nth-child(2)').textContent;
                const noOfExams = row.querySelector('td:nth-child(3)').textContent; 
                const structure = row.querySelector('td:nth-child(4)').textContent;
                const dueDate = row.querySelector('td:nth-child(5)').textContent;

                const examData = {
                    examTitle: examTitle,
                    lecturer: "Nguyen Van A",
                    course: "Database",
                    noOfExams: noOfExams,
                    structure: structure,
                    assignDate: "25/4/25",
                    dueDate: dueDate
                };

                openDetailsModal(examData);
            }
        });
    });

    // Lắng nghe sự kiện click vào nút đóng modal Details (X)
    closeDetailsModalButtons.forEach(button => {
        button.addEventListener('click', closeDetailsModal);
    });

    // Lắng nghe sự kiện click vào overlay để đóng modal (cả hai loại)
    if (modalOverlay) {
        modalOverlay.addEventListener('click', (event) => {
            if (event.target === modalOverlay) {
                if (newAssignModal.style.display === 'block') {
                    newAssignModal.style.display = 'none';
                    modalOverlay.style.display = 'none';
                    document.body.classList.remove('modal-open');
                } else if (detailsModal.style.display === 'block') {
                    closeDetailsModal();
                }
            }
        });
    }

    // Lắng nghe sự kiện nhấn phím Esc để đóng modal (cả hai loại)
    document.addEventListener('keydown', (event) => {
        if (event.key === 'Escape') {
            if (newAssignModal.style.display === 'block') {
                newAssignModal.style.display = 'none';
                modalOverlay.style.display = 'none';
                document.body.classList.remove('modal-open');
            } else if (detailsModal.style.display === 'block') {
                closeDetailsModal();
            }
        }
    });

    // Code cho New Assign modal (tái sử dụng từ phản hồi trước)
    const newAssignButton = document.querySelector('.new-assign-button');
    if (newAssignButton) {
        newAssignButton.addEventListener('click', () => {
            modalOverlay.style.display = 'block';
            newAssignModal.style.display = 'block';
            document.body.classList.add('modal-open');
        });
    }
    closeNewAssignButtons.forEach(button => {
        button.addEventListener('click', () => {
            newAssignModal.style.display = 'none';
            modalOverlay.style.display = 'none';
            document.body.classList.remove('modal-open');
        });
    });
});