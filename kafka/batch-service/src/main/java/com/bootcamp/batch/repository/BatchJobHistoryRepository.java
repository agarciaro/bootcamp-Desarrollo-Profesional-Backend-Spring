package com.bootcamp.batch.repository;

import com.bootcamp.batch.model.BatchJobHistory;
import com.bootcamp.batch.model.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BatchJobHistoryRepository extends JpaRepository<BatchJobHistory, Long> {
    
    List<BatchJobHistory> findByJobName(String jobName);
    
    List<BatchJobHistory> findByStatus(JobStatus status);
    
    Optional<BatchJobHistory> findByExecutionId(Long executionId);
    
    List<BatchJobHistory> findByJobNameOrderByStartTimeDesc(String jobName);
    
    List<BatchJobHistory> findByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT h FROM BatchJobHistory h WHERE h.jobName = :jobName AND h.startTime >= :since")
    List<BatchJobHistory> findRecentExecutions(@Param("jobName") String jobName, 
                                              @Param("since") LocalDateTime since);
    
    @Query("SELECT h FROM BatchJobHistory h WHERE h.status = 'FAILED' AND h.startTime >= :since")
    List<BatchJobHistory> findRecentFailures(@Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(h) FROM BatchJobHistory h WHERE h.jobName = :jobName AND h.status = 'COMPLETED'")
    Long countSuccessfulExecutions(@Param("jobName") String jobName);
    
    @Query("SELECT COUNT(h) FROM BatchJobHistory h WHERE h.jobName = :jobName AND h.status = 'FAILED'")
    Long countFailedExecutions(@Param("jobName") String jobName);
    
    @Query("SELECT AVG(h.recordsProcessed) FROM BatchJobHistory h WHERE h.jobName = :jobName AND h.status = 'COMPLETED'")
    Double getAverageRecordsProcessed(@Param("jobName") String jobName);
    
    @Query("SELECT h FROM BatchJobHistory h WHERE h.jobName = :jobName ORDER BY h.startTime DESC LIMIT 1")
    Optional<BatchJobHistory> findLastExecution(@Param("jobName") String jobName);
    
    @Query("SELECT h FROM BatchJobHistory h WHERE h.status = 'RUNNING'")
    List<BatchJobHistory> findCurrentlyRunningJobs();
} 