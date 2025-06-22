package com.uth.quizclear.service;

import com.uth.quizclear.repository.HEDStaticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class HEDStaticService {
    @Autowired
    private HEDStaticRepository hedStaticRepository;

    public void populateStatisticsModel(Model model) {
        model.addAllAttributes(hedStaticRepository.getStatisticsData());
    }
}
