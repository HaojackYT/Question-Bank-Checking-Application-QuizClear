package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.StaffDashboardDTO;
import com.uth.quizclear.model.dto.ChartDataDTO;
import com.uth.quizclear.model.dto.TaskDTO;
import com.uth.quizclear.model.dto.DuplicateWarningDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class StaffDashboardService {

    public StaffDashboardDTO getDashboardForStaff(Long staffId) {
        StaffDashboardDTO dashboard = new StaffDashboardDTO();
        
        // Set basic statistics with default values
        dashboard.setTotalSubjects(15);
        dashboard.setTotalQuestions(120);
        dashboard.setDuplicateQuestions(8);
        dashboard.setExamsCreated(25);
        dashboard.setSubjectsThisMonth(3);
        dashboard.setQuestionsThisMonth(18);
        dashboard.setExamsThisMonth(5);

        // Create empty chart data
        ChartDataDTO barChart = new ChartDataDTO();
        barChart.setLabels(new ArrayList<>());
        barChart.setDatasets(new ArrayList<>());
        dashboard.setBarChart(barChart);

        ChartDataDTO pieChart = new ChartDataDTO();
        pieChart.setLabels(new ArrayList<>());
        pieChart.setDatasets(new ArrayList<>());
        dashboard.setPieChart(pieChart);

        // Set empty lists
        dashboard.setRecentTasks(new ArrayList<>());
        dashboard.setDuplicateWarnings(new ArrayList<>());

        return dashboard;
    }

    public List<TaskDTO> getRecentTasks(Long staffId) {
        return new ArrayList<>();
    }

    public List<DuplicateWarningDTO> getDuplicateWarnings(Long staffId) {
        return new ArrayList<>();
    }
}
