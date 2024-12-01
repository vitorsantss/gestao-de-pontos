package com.sants.gestaodeponto.repositories;

import com.sants.gestaodeponto.domain.worklog.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkLogRepository extends JpaRepository<WorkLog, String> {
}
