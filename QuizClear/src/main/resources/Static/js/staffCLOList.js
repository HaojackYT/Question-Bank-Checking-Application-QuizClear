// // Add interactive functionality
// document.addEventListener('DOMContentLoaded', function() {
//     const modal = document.getElementById('clo-modal');
//     const modalTitle = document.getElementById('modal-title');
//     const creatorLabel = document.getElementById('creator-label');
//     const openBtn = document.querySelector('.new-clo-btn');
//     const closeBtn = document.getElementById('close-modal');
//     const editBtns = document.querySelectorAll('.action-btn.edit');
//     const deleteBtns = document.querySelectorAll('.action-btn.delete');
//     const toggleBtns = document.querySelectorAll('.toggle-btn');
//     const menuElements = document.querySelectorAll('#Menu-Staff .elements');

//     // Sidebar menu functionality
//     menuElements.forEach(element => {
//         element.addEventListener('click', function() {
//             // Remove active class from all menu items
//             menuElements.forEach(el => el.classList.remove('active'));
//             // Add active class to clicked item
//             this.classList.add('active');
//         });
//     });

//     // Open modal for creating new CLO
//     openBtn.addEventListener('click', function(e) {
//         e.preventDefault();
//         modalTitle.textContent = 'Create CLO';
//         creatorLabel.textContent = 'Created By *';
//         modal.classList.add('show');
//     });

//     // Open modal for editing CLO
//     editBtns.forEach(btn => {
//         btn.addEventListener('click', function(e) {
//             e.stopPropagation();
//             modalTitle.textContent = 'Edit CLO';
//             creatorLabel.textContent = 'Edited By *';
//             modal.classList.add('show');
//         });
//     });

//     // Close modal
//     closeBtn.addEventListener('click', function() {
//         modal.classList.remove('show');
//     });

//     // Close modal when clicking outside
//     window.addEventListener('click', function(e) {
//         if (e.target === modal) {
//             modal.classList.remove('show');
//         }
//     });

//     // Toggle buttons functionality
//     toggleBtns.forEach(btn => {
//         btn.addEventListener('click', function() {
//             toggleBtns.forEach(b => b.classList.remove('active'));
//             this.classList.add('active');
//         });
//     });

//     // Search functionality
//     const searchInput = document.querySelector('.search-input');
//     searchInput.addEventListener('input', function() {
//         console.log('Searching for:', this.value);
//         // Add your search logic here
//     });

//     // Delete button functionality
//     deleteBtns.forEach(btn => {
//         btn.addEventListener('click', function(e) {
//             e.stopPropagation();
//             if (confirm('Are you sure you want to delete this CLO?')) {
//                 console.log('Delete CLO');
//                 // Add your delete logic here
//             }
//         });
//     });

//     // Check buttons in activities
//     const checkBtns = document.querySelectorAll('.check-btn');
//     checkBtns.forEach(btn => {
//         btn.addEventListener('click', function() {
//             console.log('Check activity clicked');
//             // Add your check logic here
//         });
//     });

//     // Form submission
//     document.getElementById('clo-form').addEventListener('submit', function(e) {
//         e.preventDefault();
//         console.log('Form submitted');
//         modal.classList.remove('show');
//         // Add your form submission logic here
//     });

//     // Pagination functionality
//     const paginationBtns = document.querySelectorAll('.pagination-btn');
//     paginationBtns.forEach(btn => {
//         btn.addEventListener('click', function() {
//             if (!this.textContent.includes('Previous') && !this.textContent.includes('Next')) {
//                 paginationBtns.forEach(b => b.classList.remove('active'));
//                 this.classList.add('active');
//             }
//             console.log('Pagination clicked:', this.textContent);
//             // Add your pagination logic here
//         });
//     });

//     // Filter functionality
//     const filterSelects = document.querySelectorAll('.filter-select');
//     filterSelects.forEach(select => {
//         select.addEventListener('change', function() {
//             console.log('Filter changed:', this.value);
//             // Add your filter logic here
//         });
//     });
// });