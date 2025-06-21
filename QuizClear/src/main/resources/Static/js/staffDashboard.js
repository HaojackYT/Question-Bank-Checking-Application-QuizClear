document.addEventListener('DOMContentLoaded', function() {
    fetch('/api/dashboard/staff')
        .then(res => res.json())
        .then(data => {
            // Tổng quan
            document.querySelectorAll('.stat-card')[0].querySelector('.stat-value').textContent = data.totalSubjects;
            document.querySelectorAll('.stat-card')[0].querySelector('.stat-change').textContent = `+${data.subjectsThisMonth} this month`;
            document.querySelectorAll('.stat-card')[1].querySelector('.stat-value').textContent = data.totalQuestions;
            document.querySelectorAll('.stat-card')[1].querySelector('.stat-change').textContent = `+${data.questionsThisMonth} this month`;
            document.querySelectorAll('.stat-card')[2].querySelector('.stat-value').textContent = data.duplicateQuestions;
            document.querySelectorAll('.stat-card')[2].querySelector('.stat-change').textContent = 'Needs review';
            document.querySelectorAll('.stat-card')[3].querySelector('.stat-value').textContent = data.examsCreated;
            document.querySelectorAll('.stat-card')[3].querySelector('.stat-change').textContent = `+${data.examsThisMonth} this month`;

            // Bar Chart
            if (window.barChart && typeof window.barChart.destroy === 'function') {
                window.barChart.destroy();
            }
            const barCtx = document.getElementById('barChart').getContext('2d');
            window.barChart = new Chart(barCtx, {
                type: 'bar',
                data: {
                    labels: data.barChart.labels.map(() => ''), // Ẩn hoàn toàn nhãn trục X
                    datasets: data.barChart.datasets.map(ds => ({
                        label: ds.label,
                        data: ds.data,
                        backgroundColor: ds.backgroundColor,
                        borderRadius: 6,
                        barThickness: 32
                    }))
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    layout: { padding: { top: 24, bottom: 0, left: 0, right: 0 } },
                    plugins: {
                        legend: {
                            display: true,
                            position: 'bottom',
                            labels: {
                                boxWidth: 18,
                                boxHeight: 18,
                                font: { size: 16, weight: 'bold' },
                                color: '#3B2175',
                                padding: 24
                            }
                        },
                        title: { display: false },
                        tooltip: {
                            enabled: true,
                            backgroundColor: '#fff',
                            titleColor: '#3B2175',
                            bodyColor: '#3B2175',
                            borderColor: '#eee',
                            borderWidth: 1,
                            titleFont: { size: 16 },
                            bodyFont: { size: 15 }
                        }
                    },
                    scales: {
                        x: {
                            grid: { display: false, drawBorder: false },
                            ticks: {
                                display: false, // Ẩn hoàn toàn chữ dưới trục X
                                maxRotation: 0,
                                minRotation: 0,
                                padding: 8
                            }
                        },
                        y: {
                            beginAtZero: true,
                            grid: { color: '#eee', drawBorder: false },
                            border: { display: false },
                            ticks: {
                                color: '#888',
                                font: { size: 14 },
                                stepSize: 20,
                                padding: 8
                            }
                        }
                    }
                }
            });

            // Pie Chart
            if (window.pieChart && typeof window.pieChart.destroy === 'function') {
                window.pieChart.destroy();
            }
            const pieCtx = document.getElementById('pieChart').getContext('2d');
            window.pieChart = new Chart(pieCtx, {
                type: 'pie',
                data: {
                    labels: data.pieChart.labels,
                    datasets: data.pieChart.datasets.map(ds => ({
                        data: ds.data,
                        backgroundColor: ['#8979FF', '#FF928A', '#3CC3DF', '#FFAE4C']
                    }))
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: { legend: { display: true, position: 'right' } }
                }
            });

            // Recent Tasks
            const tasksContainer = document.querySelector('.recent-tasks-container');
            tasksContainer.querySelectorAll('.task-item').forEach(e => e.remove());
            let showingAll = false;
            function renderTasks(list) {
                tasksContainer.querySelectorAll('.task-item').forEach(e => e.remove());
                list.forEach(task => {
                    const div = document.createElement('div');
                    div.className = 'task-item';
                    let statusClass = '';
                    let statusColor = '';
                    if (task.status === 'In Progress') { statusClass = 'in-progress'; statusColor = '#0088F0'; }
                    else if (task.status === 'Pending') { statusClass = 'pending'; statusColor = '#FFA500'; }
                    else { statusClass = ''; statusColor = '#FFA500'; }
                    div.innerHTML = `
                        <div class="task-icon"><svg width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="${statusColor}" stroke-width="2"><circle cx="12" cy="12" r="10" /><polyline points="12,6 12,12 16,14" /></svg></div>
                        <div class="task-content">
                            <div class="task-title">${task.title}</div>
                            <div class="task-due">Due: ${task.dueDate ? new Date(task.dueDate).toLocaleDateString() : ''}</div>
                        </div>
                        <div class="task-status ${statusClass}">${task.status}</div>
                    `;
                    tasksContainer.insertBefore(div, tasksContainer.querySelector('.see-more'));
                });
            }
            renderTasks(data.recentTasks);
            // See more
            const seeMoreBtn = tasksContainer.querySelector('.see-more');
            if (seeMoreBtn) {
                seeMoreBtn.onclick = function() {
                    if (!showingAll) {
                        renderTasks(data.allTasks);
                        seeMoreBtn.textContent = 'See less...';
                        showingAll = true;
                    } else {
                        renderTasks(data.recentTasks);
                        seeMoreBtn.textContent = 'See more...';
                        showingAll = false;
                    }
                };
            }

            // Duplicate Warnings
            const dupContainer = document.querySelector('.duplicate-warning-container');
            dupContainer.querySelectorAll('.duplicate-item').forEach(e => e.remove());
            dupContainer.querySelector('.count-value').textContent = data.duplicateQuestions;
            data.duplicateWarnings.forEach(warn => {
                const div = document.createElement('div');
                div.className = 'duplicate-item';
                div.innerHTML = `
                    <div class="duplicate-header">
                        <div class="similarity-text">Similarity: ${Math.round(warn.similarity)}%</div>
                        <div class="warning-icon"><svg width="30" height="30" viewBox="0 0 24 24" fill="none" stroke="#EF2A2D" stroke-width="2"><path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z" /><path d="M12 9v4" /><path d="m12 17 .01 0" /></svg></div>
                    </div>
                    <div class="question-box"><div class="question-text">${warn.question1}</div></div>
                    <div class="question-box"><div class="question-text">${warn.question2}</div></div>
                `;
                dupContainer.insertBefore(div, dupContainer.querySelector('.see-more'));
            });
        });
});
