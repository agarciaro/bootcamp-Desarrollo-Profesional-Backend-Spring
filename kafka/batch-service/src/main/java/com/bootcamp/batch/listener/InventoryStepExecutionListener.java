package com.bootcamp.batch.listener;

import com.bootcamp.batch.model.BatchJobHistory;
import com.bootcamp.batch.model.JobStatus;
import com.bootcamp.batch.repository.BatchJobHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InventoryStepExecutionListener implements StepExecutionListener {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryStepExecutionListener.class);
    
    @Autowired
    private BatchJobHistoryRepository batchJobHistoryRepository;
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.info("Starting step: {} with execution ID: {}", 
                   stepExecution.getStepName(), 
                   stepExecution.getId());
        
        // Log step parameters
        stepExecution.getExecutionContext().entrySet().forEach(entry -> 
            logger.debug("Step parameter - {}: {}", entry.getKey(), entry.getValue())
        );
    }
    
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("Completed step: {} with status: {}", 
                   stepExecution.getStepName(), 
                   stepExecution.getExitStatus());
        
        // Log step statistics
        logger.info("Step statistics - Read: {}, Written: {}, Skipped: {}, Filtered: {}", 
                   stepExecution.getReadCount(),
                   stepExecution.getWriteCount(),
                   stepExecution.getSkipCount(),
                   stepExecution.getFilterCount());
        
        // Log errors if any
        if (stepExecution.getFailureExceptions() != null && !stepExecution.getFailureExceptions().isEmpty()) {
            logger.error("Step failed with {} exceptions", stepExecution.getFailureExceptions().size());
            stepExecution.getFailureExceptions().forEach(exception -> 
                logger.error("Step failure: {}", exception.getMessage(), exception)
            );
        }
        
        // TODO: Log skipped exceptions if needed
        // Note: getSkipExceptions() method doesn't exist in StepExecution
        // You would need to track skipped items manually if required
        
        // Save step execution history
        saveStepExecutionHistory(stepExecution);
        
        return stepExecution.getExitStatus();
    }
    
    private void saveStepExecutionHistory(StepExecution stepExecution) {
        try {
            BatchJobHistory history = new BatchJobHistory();
            history.setJobName(stepExecution.getJobExecution().getJobInstance().getJobName());
            history.setExecutionId(stepExecution.getJobExecution().getId());
            history.setStatus(convertExitStatusToJobStatus(stepExecution.getExitStatus()));
            history.setStartTime(stepExecution.getStartTime());
            history.setEndTime(stepExecution.getEndTime());
            history.setRecordsProcessed((int) stepExecution.getReadCount());
            history.setRecordsSkipped((int) stepExecution.getSkipCount());
            history.setCreatedAt(LocalDateTime.now());
            
            batchJobHistoryRepository.save(history);
            logger.debug("Saved step execution history for step: {}", stepExecution.getStepName());
        } catch (Exception e) {
            logger.error("Failed to save step execution history: {}", e.getMessage(), e);
        }
    }
    
    private JobStatus convertExitStatusToJobStatus(ExitStatus exitStatus) {
        if (exitStatus == null) {
            return JobStatus.ABANDONED;
        }
        
        switch (exitStatus.getExitCode()) {
            case "COMPLETED":
                return JobStatus.COMPLETED;
            case "FAILED":
                return JobStatus.FAILED;
            case "STOPPED":
                return JobStatus.STOPPED;
            default:
                return JobStatus.ABANDONED;
        }
    }
} 