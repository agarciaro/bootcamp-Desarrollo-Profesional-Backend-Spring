package com.bootcamp.batch.listener;

import com.bootcamp.batch.model.BatchJobHistory;
import com.bootcamp.batch.model.JobStatus;
import com.bootcamp.batch.repository.BatchJobHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InventoryJobExecutionListener implements JobExecutionListener {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryJobExecutionListener.class);
    
    @Autowired
    private BatchJobHistoryRepository jobHistoryRepository;
    
    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("Starting job: {} with execution ID: {}", 
                   jobExecution.getJobInstance().getJobName(), 
                   jobExecution.getId());
        
        // Create job history record
        BatchJobHistory history = new BatchJobHistory(
            jobExecution.getJobInstance().getJobName(),
            jobExecution.getJobInstance().getId(),
            jobExecution.getId()
        );
        history.setStatus(JobStatus.RUNNING);
        jobHistoryRepository.save(history);
        
        // Log job parameters
        logger.info("Job parameters: {}", jobExecution.getJobParameters());
    }
    
    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        Long executionId = jobExecution.getId();
        
        // Update job history
        BatchJobHistory history = jobHistoryRepository.findByExecutionId(executionId)
                .orElse(new BatchJobHistory(jobName, jobExecution.getJobInstance().getId(), executionId));
        
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("Job completed successfully: {} (Execution ID: {})", jobName, executionId);
            history.complete();
            history.setExitCode("COMPLETED");
            history.setExitMessage("Job completed successfully");
        } else {
            logger.error("Job failed: {} (Execution ID: {}) - Status: {}", 
                        jobName, executionId, jobExecution.getStatus());
            history.fail("FAILED", jobExecution.getAllFailureExceptions().isEmpty() ? 
                        "Unknown error" : jobExecution.getAllFailureExceptions().get(0).getMessage());
        }
        
        // Set execution statistics
        history.setRecordsProcessed((int) jobExecution.getStepExecutions().stream()
                .mapToLong(step -> step.getReadCount()).sum());
        history.setRecordsSkipped((int) jobExecution.getStepExecutions().stream()
                .mapToLong(step -> step.getSkipCount()).sum());
        history.setRecordsFailed((int) jobExecution.getStepExecutions().stream()
                .mapToLong(step -> step.getFilterCount()).sum());
        
        jobHistoryRepository.save(history);
        
        // Log final statistics
        logger.info("Job execution summary for {} (ID: {}):", jobName, executionId);
        logger.info("  - Status: {}", jobExecution.getStatus());
        logger.info("  - Start Time: {}", jobExecution.getStartTime());
        logger.info("  - End Time: {}", jobExecution.getEndTime());
        logger.info("  - Duration: {} seconds", 
                   jobExecution.getEndTime() != null && jobExecution.getStartTime() != null ?
                   java.time.Duration.between(jobExecution.getStartTime(), jobExecution.getEndTime()).getSeconds() : "N/A");
        logger.info("  - Records Processed: {}", history.getRecordsProcessed());
        logger.info("  - Records Skipped: {}", history.getRecordsSkipped());
        logger.info("  - Records Failed: {}", history.getRecordsFailed());
        
        // Log any exceptions
        if (!jobExecution.getAllFailureExceptions().isEmpty()) {
            logger.error("Job execution exceptions:");
            jobExecution.getAllFailureExceptions().forEach(exception -> 
                logger.error("  - {}", exception.getMessage(), exception));
        }
    }
} 