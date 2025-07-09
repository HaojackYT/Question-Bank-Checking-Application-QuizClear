package com.uth.quizclear.service;

import java.util.List;
import java.util.Map;

public interface HOEDashboardService {
    int getPendingAssignments();
    int getTotalReviews();
    int getNeedApproval();
    int getTaskCompletion();
    int getOverallProgress();
    List<Map<String, Object>> getChartData();
    List<Map<String, Object>> getRecentUpdates();
}
