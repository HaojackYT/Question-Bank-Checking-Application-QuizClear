package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SL_detailTaskRepository extends JpaRepository<Tasks, Integer> {

    @Query("SELECT q FROM Question q WHERE q.taskId = :taskId")
    List<Question> findQuestionsByTaskId(@Param("taskId") Integer taskId);
}