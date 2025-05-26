function loadCSS(href) {
    let link = document.querySelector('link[data-tab-css]');
    if (link) {
        link.href = href;
    } else {
        link = document.createElement('link');
        link.rel = 'stylesheet';
        link.href = href;
        link.setAttribute('data-tab-css', 'true');
        document.head.appendChild(link);
    }
}

function bindTabEvents() {
    document.querySelectorAll('.tab').forEach(tab => {
        tab.addEventListener('click', function () {
            document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
            this.classList.add('active');

            const tabName = this.dataset.tab;

            const contentArea = document.getElementById('tab-content');
            let fileToLoad = '';
            let cssToLoad = '';

            switch (tabName) {
                case 'detection':
                    fileToLoad = 'content/indexContent.html';
                    cssToLoad = 'style/styles.css';
                    break;
                case 'stat':
                    fileToLoad = 'content/statContent.html';
                    cssToLoad = 'style/stat.css';
                    break;
                case 'proc_log':
                    fileToLoad = 'content/logContent.html';
                    cssToLoad = 'style/logs.css';
                    break;
            }

            if (fileToLoad) {
                fetch(fileToLoad)
                    .then(res => res.text())
                    .then(html => {
                        contentArea.innerHTML = html;
                        if (cssToLoad) loadCSS(cssToLoad);

                        // Gắn lại sự kiện cho các button/filter bên trong tab, nếu cần
                        bindInnerEvents();
                    })
                    .catch(err => {
                        contentArea.innerHTML = '<p>Error loading content.</p>';
                        console.error('Error:', err);
                    });
            }
        });
    });
}

function bindInnerEvents() {
    document.querySelectorAll('.filter-select').forEach(select => {
        select.addEventListener('change', function () {
            console.log('Filter changed:', this.value);
        });
    });

    const exportBtn = document.querySelector('.export-btn');
    if (exportBtn) {
        exportBtn.addEventListener('click', function () {
            alert('Export clicked');
        });
    }
}

// Gắn sự kiện khi trang load xong
document.addEventListener('DOMContentLoaded', () => {
    bindTabEvents();

    // Load mặc định tab đầu tiên (nếu muốn)
    document.querySelector('.tab.active').click();
});

document.addEventListener('DOMContentLoaded', () => {
  bindTabEvents();

  const firstTab = document.querySelector('.tab.active') || document.querySelector('.tab');
  if (firstTab) firstTab.click();
});


const viewButtons = document.querySelectorAll('.btn-view');
const detectionSection = document.querySelector('.detection-section');
const comparisonContainer = document.getElementById('comparison-container'); // nên tạo sẵn thẻ div rỗng này trong HTML

viewButtons.forEach(btn => {
  btn.addEventListener('click', async () => {
    try {
      detectionSection.style.display = 'none';

      // Nếu chưa tải thì mới fetch
      if (comparisonContainer.innerHTML.trim() === '') {
        const res = await fetch('dupDetails.html');
        if (!res.ok) throw new Error('Cannot load dupDetails.html');
        const html = await res.text();

        // Tạo thẻ tạm để parse HTML
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = html;

        // 1. Thêm CSS nếu có thẻ <style href="">
        const styleTag = tempDiv.querySelector('style[href]');
        if (styleTag) {
          const href = styleTag.getAttribute('href');
          const linkEl = document.createElement('link');
          linkEl.rel = 'stylesheet';
          linkEl.href = href;
          document.head.appendChild(linkEl);
          styleTag.remove(); // xóa thẻ <style href=""> giả
        }

        // 2. Thêm phần còn lại vào container
        comparisonContainer.innerHTML = tempDiv.innerHTML;

        // 3. Gán lại sự kiện cho nút Back
        const backLink = comparisonContainer.querySelector('.back-link');
        backLink.addEventListener('click', e => {
          e.preventDefault();
          comparisonContainer.style.display = 'none';
          detectionSection.style.display = 'block';
        });
      }

      comparisonContainer.style.display = 'block';

    } catch (e) {
      alert(e.message);
      detectionSection.style.display = 'block';
      comparisonContainer.style.display = 'none';
    }
  });
});

