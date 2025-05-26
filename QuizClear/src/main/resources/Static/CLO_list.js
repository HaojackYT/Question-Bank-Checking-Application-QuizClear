
        // Add interactive functionality
        document.addEventListener('DOMContentLoaded', function() {
            const modal = document.getElementById('clo-modal');
            const modalTitle = document.getElementById('modal-title');
            const creatorLabel = document.getElementById('creator-label');
            const openBtn = document.querySelector('.new-clo-btn');
            const closeBtn = document.getElementById('close-modal');
            const editBtns = document.querySelectorAll('.action-btn.edit');
            const deleteBtns = document.querySelectorAll('.action-btn.delete');
            const toggleBtns = document.querySelectorAll('.toggle-btn');

            // Open modal for creating new CLO
            openBtn.addEventListener('click', function(e) {
                e.preventDefault();
                modalTitle.textContent = 'Create CLO';
                creatorLabel.textContent = 'Created By *';
                modal.classList.add('show');
            });

            // Open modal for editing CLO
            editBtns.forEach(btn => {
                btn.addEventListener('click', function(e) {
                    e.stopPropagation();
                    modalTitle.textContent = 'Edit CLO';
                    creatorLabel.textContent = 'Edited By *';
                    modal.classList.add('show');
                });
            });

            // Close modal
            closeBtn.addEventListener('click', function() {
                modal.classList.remove('show');
            });

            // Close modal when clicking outside
            window.addEventListener('click', function(e) {
                if (e.target === modal) {
                    modal.classList.remove('show');
                }
            });

            // Toggle buttons functionality
            toggleBtns.forEach(btn => {
                btn.addEventListener('click', function() {
                    toggleBtns.forEach(b => b.classList.remove('active'));
                    this.classList.add('active');
                });
            });

            // Search functionality
            const searchInput = document.querySelector('.search-input');
            searchInput.addEventListener('input', function() {
                console.log('Searching for:', this.value);
            });

            // Delete button functionality
            deleteBtns.forEach(btn => {
                btn.addEventListener('click', function(e) {
                    e.stopPropagation();
                    if (confirm('Are you sure you want to delete this CLO?')) {
                        console.log('Delete CLO');
                    }
                });
            });

            // Check buttons in activities
            const checkBtns = document.querySelectorAll('.check-btn');
            checkBtns.forEach(btn => {
                btn.addEventListener('click', function() {
                    console.log('Check activity clicked');
                });
            });

            // Form submission
            document.getElementById('clo-form').addEventListener('submit', function(e) {
                e.preventDefault();
                console.log('Form submitted');
                modal.classList.remove('show');
            });
        });
  