package com.uth.quizclear.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.SL_PlanDTO;
import com.uth.quizclear.model.entity.Plan;
import com.uth.quizclear.repository.PlanRepository;

@Service
public class PlanService {

    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    // Lấy toàn bộ plans
    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    // Lấy danh sách plan cho SL
    public List<SL_PlanDTO> getSLPlan() {
        return planRepository.findSLPlans(List.of(Plan.PlanStatus.NEW, Plan.PlanStatus.ACCEPTED));
    }
}

