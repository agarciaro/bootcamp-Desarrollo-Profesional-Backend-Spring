package com.bootcamp.batch.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "batch_job_history")
public class BatchJobHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "job_name", nullable = false, length = 100)
    private String jobName;
    
    @Column(name = "job_instance_id")
    private Long jobInstanceId;
    
    @Column(name = "execution_id")
    private Long executionId;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private JobStatus status;
    
    @Column(name = "exit_code", length = 20)
    private String exitCode;
    
    @Column(name = "exit_message", columnDefinition = "TEXT")
    private String exitMessage;
    
    @Column(name = "records_processed")
    private Integer recordsProcessed = 0;
    
    @Column(name = "records_skipped")
    private Integer recordsSkipped = 0;
    
    @Column(name = "records_failed")
    private Integer recordsFailed = 0;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public BatchJobHistory() {
        this.createdAt = LocalDateTime.now();
    }
    
    public BatchJobHistory(String jobName, Long jobInstanceId, Long executionId) {
        this();
        this.jobName = jobName;
        this.jobInstanceId = jobInstanceId;
        this.executionId = executionId;
        this.status = JobStatus.STARTING;
        this.startTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getJobName() {
        return jobName;
    }
    
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    
    public Long getJobInstanceId() {
        return jobInstanceId;
    }
    
    public void setJobInstanceId(Long jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }
    
    public Long getExecutionId() {
        return executionId;
    }
    
    public void setExecutionId(Long executionId) {
        this.executionId = executionId;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public JobStatus getStatus() {
        return status;
    }
    
    public void setStatus(JobStatus status) {
        this.status = status;
    }
    
    public String getExitCode() {
        return exitCode;
    }
    
    public void setExitCode(String exitCode) {
        this.exitCode = exitCode;
    }
    
    public String getExitMessage() {
        return exitMessage;
    }
    
    public void setExitMessage(String exitMessage) {
        this.exitMessage = exitMessage;
    }
    
    public Integer getRecordsProcessed() {
        return recordsProcessed;
    }
    
    public void setRecordsProcessed(Integer recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
    }
    
    public Integer getRecordsSkipped() {
        return recordsSkipped;
    }
    
    public void setRecordsSkipped(Integer recordsSkipped) {
        this.recordsSkipped = recordsSkipped;
    }
    
    public Integer getRecordsFailed() {
        return recordsFailed;
    }
    
    public void setRecordsFailed(Integer recordsFailed) {
        this.recordsFailed = recordsFailed;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Business methods
    public void complete() {
        this.status = JobStatus.COMPLETED;
        this.endTime = LocalDateTime.now();
    }
    
    public void fail(String exitCode, String exitMessage) {
        this.status = JobStatus.FAILED;
        this.exitCode = exitCode;
        this.exitMessage = exitMessage;
        this.endTime = LocalDateTime.now();
    }
    
    public Long getDurationInSeconds() {
        if (startTime != null && endTime != null) {
            return java.time.Duration.between(startTime, endTime).getSeconds();
        }
        return null;
    }
    
    public Integer getTotalRecords() {
        return (recordsProcessed != null ? recordsProcessed : 0) +
               (recordsSkipped != null ? recordsSkipped : 0) +
               (recordsFailed != null ? recordsFailed : 0);
    }
    
    @Override
    public String toString() {
        return "BatchJobHistory{" +
                "id=" + id +
                ", jobName='" + jobName + '\'' +
                ", executionId=" + executionId +
                ", status=" + status +
                ", recordsProcessed=" + recordsProcessed +
                ", recordsSkipped=" + recordsSkipped +
                ", recordsFailed=" + recordsFailed +
                '}';
    }
} 