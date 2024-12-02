package com.sants.gestaodeponto.repositories;

import com.sants.gestaodeponto.domain.worklog.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkLogRepository extends JpaRepository<WorkLog, String> {

    List<WorkLog> findByUserIdAndTimestampAfter(String userId, LocalDateTime timestamp);

    List<WorkLog> findByUserIdOrderByTimestampDesc(String userId);

    List<WorkLog> findByUserId(String userId);
}
