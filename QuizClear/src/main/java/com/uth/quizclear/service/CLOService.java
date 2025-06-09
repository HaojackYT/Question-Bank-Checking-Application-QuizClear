package com.uth.quizclear.service;

import com.uth.quizclear.model.CLO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CLOService {
    Page<CLO> getCLOs(String keyword, String department, String difficultyLevel, Pageable pageable);

    CLO getCLOById(Integer id);

    CLO createNewCLO(CLO clo);

    CLO updateCLO(Integer id, CLO cloDetails);

    void deleteCLO(Integer id);

    CLO updateCLOStatus(Integer id, String status);

    Integer getQuestionsCountForCLO(Integer cloId);
}