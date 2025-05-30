
// Lấy input tổng số câu hỏi
const totalQuestionsInput = document.getElementById("total-questions");

// Cấu hình các slider và cột tương ứng
const sliders = [
    {
        range: document.getElementById("range-basic"),
        valueEl: document.getElementById("range-value-basic"),
        percentEl: document.querySelector("#range-basic").parentElement.querySelector("p span"),
        columEl: document.getElementById("Basic"),
        valuePercentEl: document.getElementById("value-basic")
    },
    {
        range: document.getElementById("range-Intermediate"),
        valueEl: document.getElementById("range-value-Intermediate"),
        percentEl: document.querySelector("#range-Intermediate").parentElement.querySelector("p span"),
        columEl: document.getElementById("Intermediate"),
        valuePercentEl: document.getElementById("value-intermediate")
    },
    {
        range: document.getElementById("range-Advanced"),
        valueEl: document.getElementById("range-value-Advanced"),
        percentEl: document.querySelector("#range-Advanced").parentElement.querySelector("p span"),
        columEl: document.getElementById("Advanced"),
        valuePercentEl: document.getElementById("value-advanced")
    },
    {
        range: document.getElementById("range-Expert"),
        valueEl: document.getElementById("range-value-Expert"),
        percentEl: document.querySelector("#range-Expert").parentElement.querySelector("p span"),
        columEl: document.getElementById("Expert"),
        valuePercentEl: document.getElementById("value-expert")
    }
];

// Lấy thẻ hiển thị tổng phần trăm
const totalPercentageDisplay = document.querySelector("p[style*='color: #2563EB'] span");

// Hàm tính tổng số câu hỏi từ các thanh kéo
function getTotalSliderValue() {
    return sliders.reduce((sum, s) => sum + parseInt(s.range.value), 0);
}

// Hàm cập nhật phần trăm, số câu hỏi và độ cao cột
function updateSliderValues(sliderObj, totalQuestions) {
    const value = parseInt(sliderObj.range.value);
    sliderObj.valueEl.textContent = value; // Hiển thị số câu hỏi
    const percentage = totalQuestions > 0 ? ((value / totalQuestions) * 100).toFixed(1) : 0;
    sliderObj.percentEl.textContent = percentage; // Hiển thị phần trăm trên thanh kéo
    sliderObj.valuePercentEl.textContent = percentage; // Hiển thị phần trăm dưới cột
    sliderObj.columEl.style.height = `${percentage}px`; // Cập nhật độ cao cột (1% = 1px)
}

// Hàm cập nhật tổng phần trăm
function updateTotalPercentage() {
    const totalQuestions = parseInt(totalQuestionsInput.value) || 0;
    const totalSliderValue = getTotalSliderValue();
    const totalPercentage = totalQuestions > 0 ? ((totalSliderValue / totalQuestions) * 100).toFixed(1) : 0;
    totalPercentageDisplay.textContent = totalPercentage; // Cập nhật tổng phần trăm
}

// Hàm cập nhật màu nền thanh kéo
function updateSliderBackground(slider) {
    const min = slider.min ? parseFloat(slider.min) : 0;
    const max = slider.max ? parseFloat(slider.max) : 100;
    const val = ((slider.value - min) / (max - min)) * 100;
    slider.style.background = `linear-gradient(to right, #3B82F6 ${val}%, #DBEAFE ${val}%)`;
}

// Xử lý sự kiện cho từng thanh kéo
sliders.forEach((sliderObj) => {
    sliderObj.range.addEventListener("input", () => {
        const totalQuestions = parseInt(totalQuestionsInput.value) || 0;
        const currentTotal = getTotalSliderValue();
        const previousValue = parseInt(sliderObj.valueEl.textContent);
        const currentValue = parseInt(sliderObj.range.value);

        // Nếu tổng bằng hoặc vượt quá totalQuestions và đang tăng, giữ nguyên giá trị cũ
        if (currentValue > previousValue && currentTotal > totalQuestions) {
            sliderObj.range.value = previousValue;
            return;
        }

        // Cập nhật giá trị hiển thị, phần trăm và độ cao cột
        updateSliderValues(sliderObj, totalQuestions);
        updateTotalPercentage();
        updateSliderBackground(sliderObj.range);
    });

    // Cập nhật màu nền ban đầu
    updateSliderBackground(sliderObj.range);
});

// Xử lý khi nhập total-questions
totalQuestionsInput.addEventListener("input", () => {
    const total = parseInt(totalQuestionsInput.value);
    if (isNaN(total) || total < 0) {
        totalQuestionsInput.value = 0;
    }
    const totalQuestions = parseInt(totalQuestionsInput.value) || 0;
    const currentTotal = getTotalSliderValue();

    // Nếu tổng câu hỏi hiện tại vượt quá totalQuestions, giảm giá trị các thanh kéo
    if (currentTotal > totalQuestions) {
        let excess = currentTotal - totalQuestions;
        for (let i = sliders.length - 1; i >= 0 && excess > 0; i--) {
            const currentValue = parseInt(sliders[i].range.value);
            const reduction = Math.min(currentValue, excess);
            sliders[i].range.value = currentValue - reduction;
            excess -= reduction;
            updateSliderValues(sliders[i], totalQuestions);
            updateSliderBackground(sliders[i].range);
        }
    }

    // Cập nhật lại tất cả giá trị hiển thị và độ cao cột
    sliders.forEach(sliderObj => updateSliderValues(sliderObj, totalQuestions));
    updateTotalPercentage();
});

// Khởi tạo giá trị ban đầu
sliders.forEach(sliderObj => updateSliderValues(sliderObj, parseInt(totalQuestionsInput.value) || 0));
updateTotalPercentage();

