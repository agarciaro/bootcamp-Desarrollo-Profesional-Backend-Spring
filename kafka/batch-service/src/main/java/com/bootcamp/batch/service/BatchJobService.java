package com.bootcamp.batch.service;

import com.bootcamp.batch.dto.JobExecutionRequest;
import com.bootcamp.batch.model.BatchJobHistory;
import com.bootcamp.batch.model.JobStatus;
import com.bootcamp.batch.repository.BatchJobHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BatchJobService {
    
    private static final Logger logger = LoggerFactory.getLogger(BatchJobService.class);
    
    @Autowired
    private JobLauncher jobLauncher;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private JobExplorer jobExplorer;
    
    @Autowired
    private JobOperator jobOperator;
    
    @Autowired
    private BatchJobHistoryRepository batchJobHistoryRepository;
    
    @Autowired
    @Qualifier("inventoryProcessingJob")
    private Job inventoryProcessingJob;
    
    public JobExecution executeJob(JobExecutionRequest request) throws Exception {
        logger.info("Executing job: {} with parameters: {}", request.getJobName(), request.getParameters());
        
        JobParameters jobParameters = buildJobParameters(request);
        Job job = getJobByName(request.getJobName());
        
        if (job == null) {
            throw new IllegalArgumentException("Job not found: " + request.getJobName());
        }
        
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        
        // Save job execution history
        saveJobExecutionHistory(jobExecution);
        
        logger.info("Job execution started with ID: {}", jobExecution.getId());
        return jobExecution;
    }
    
    public JobExecution getJobExecution(Long executionId) {
        return jobRepository.getLastJobExecution(executionId.toString(), new JobParameters());
    }
    
    public JobExecution restartJob(Long executionId) throws Exception {
        logger.info("Restarting job execution: {}", executionId);
        
        try {
            // Use JobOperator to restart the job
            Long newExecutionId = jobOperator.restart(executionId);
            JobExecution newExecution = jobRepository.getLastJobExecution(newExecutionId.toString(), new JobParameters());
            
            logger.info("Job restarted successfully. New execution ID: {}", newExecutionId);
            return newExecution;
        } catch (Exception e) {
            logger.error("Failed to restart job execution {}: {}", executionId, e.getMessage(), e);
            throw new RuntimeException("Failed to restart job: " + e.getMessage(), e);
        }
    }
    
    public void stopJob(Long executionId) throws Exception {
        logger.info("Stopping job execution: {}", executionId);
        
        try {
            // Use JobOperator to stop the job
            boolean stopped = jobOperator.stop(executionId);
            
            if (stopped) {
                logger.info("Job execution {} stopped successfully", executionId);
            } else {
                logger.warn("Job execution {} was already stopped or not running", executionId);
            }
        } catch (Exception e) {
            logger.error("Failed to stop job execution {}: {}", executionId, e.getMessage(), e);
            throw new RuntimeException("Failed to stop job: " + e.getMessage(), e);
        }
    }
    
    public List<JobExecution> getJobHistory(String jobName) {
        logger.info("Getting job history for: {}", jobName);
        
        List<JobExecution> executions = new ArrayList<>();
        
        // Get job instances
        List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, 0, Integer.MAX_VALUE);
        
        for (JobInstance jobInstance : jobInstances) {
            List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
            executions.addAll(jobExecutions);
        }
        
        // Sort by start time descending
        executions.sort((e1, e2) -> e2.getStartTime().compareTo(e1.getStartTime()));
        
        return executions;
    }
    
    public List<JobExecution> getRecentJobHistory(String jobName, int days) {
        logger.info("Getting recent job history for: {} (last {} days)", jobName, days);
        
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        List<JobExecution> allExecutions = getJobHistory(jobName);
        
        return allExecutions.stream()
                .filter(execution -> execution.getStartTime() != null && 
                                   execution.getStartTime().isAfter(cutoffDate))
                .toList();
    }
    
    public Map<String, Object> getJobStatistics(String jobName) {
        logger.info("Getting job statistics for: {}", jobName);
        
        List<JobExecution> executions = getJobHistory(jobName);
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalExecutions", executions.size());
        
        if (!executions.isEmpty()) {
            long successfulExecutions = executions.stream()
                    .filter(execution -> ExitStatus.COMPLETED.equals(execution.getExitStatus()))
                    .count();
            
            long failedExecutions = executions.stream()
                    .filter(execution -> ExitStatus.FAILED.equals(execution.getExitStatus()))
                    .count();
            
            statistics.put("successfulExecutions", successfulExecutions);
            statistics.put("failedExecutions", failedExecutions);
            statistics.put("successRate", (double) successfulExecutions / executions.size());
            
            // Calculate average duration
            double avgDuration = executions.stream()
                    .filter(execution -> execution.getStartTime() != null && execution.getEndTime() != null)
                    .mapToLong(execution -> 
                        java.time.Duration.between(execution.getStartTime(), execution.getEndTime()).toSeconds())
                    .average()
                    .orElse(0.0);
            
            statistics.put("averageDurationSeconds", avgDuration);
        }
        
        return statistics;
    }
    
    private JobParameters buildJobParameters(JobExecutionRequest request) {
        JobParametersBuilder builder = new JobParametersBuilder();
        
        if (request.getParameters() != null) {
            request.getParameters().forEach((key, value) -> {
                if (value instanceof String) {
                    builder.addString(key, (String) value);
                } else if (value instanceof Long) {
                    builder.addLong(key, (Long) value);
                } else if (value instanceof Double) {
                    builder.addDouble(key, (Double) value);
                } else {
                    builder.addString(key, value.toString());
                }
            });
        }
        
        // Add timestamp to ensure unique job execution
        builder.addLong("timestamp", System.currentTimeMillis());
        
        return builder.toJobParameters();
    }
    
    private Job getJobByName(String jobName) {
        if ("inventoryProcessingJob".equals(jobName)) {
            return inventoryProcessingJob;
        }
        return null;
    }
    
    private void saveJobExecutionHistory(JobExecution jobExecution) {
        try {
            BatchJobHistory history = new BatchJobHistory();
            history.setJobName(jobExecution.getJobInstance().getJobName());
            history.setExecutionId(jobExecution.getId());
            history.setStatus(convertExitStatusToJobStatus(jobExecution.getExitStatus()));
            history.setStartTime(jobExecution.getStartTime());
            history.setEndTime(jobExecution.getEndTime());
            history.setCreatedAt(LocalDateTime.now());
            
            batchJobHistoryRepository.save(history);
            logger.debug("Saved job execution history for job: {}", 
                        jobExecution.getJobInstance().getJobName());
        } catch (Exception e) {
            logger.error("Failed to save job execution history: {}", e.getMessage(), e);
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