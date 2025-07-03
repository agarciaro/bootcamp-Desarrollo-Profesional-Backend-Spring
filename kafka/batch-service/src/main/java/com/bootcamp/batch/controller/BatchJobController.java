package com.bootcamp.batch.controller;

import com.bootcamp.batch.dto.JobExecutionRequest;
import com.bootcamp.batch.model.BatchJobHistory;
import com.bootcamp.batch.service.BatchJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/batch/jobs")
public class BatchJobController {
    
    private static final Logger logger = LoggerFactory.getLogger(BatchJobController.class);
    
    @Autowired
    private BatchJobService batchJobService;
    
    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> executeJob(@Valid @RequestBody JobExecutionRequest request) {
        try {
            logger.info("Received job execution request: {}", request.getJobName());
            
            JobExecution jobExecution = batchJobService.executeJob(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Job execution started successfully");
            response.put("jobName", request.getJobName());
            response.put("executionId", jobExecution.getId());
            response.put("status", jobExecution.getStatus());
            response.put("startTime", jobExecution.getStartTime());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error executing job: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to execute job");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @PostMapping("/{executionId}/restart")
    public ResponseEntity<Map<String, Object>> restartJob(@PathVariable Long executionId) {
        try {
            logger.info("Received job restart request for execution ID: {}", executionId);
            
            JobExecution jobExecution = batchJobService.restartJob(executionId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Job restart started successfully");
            response.put("previousExecutionId", executionId);
            response.put("newExecutionId", jobExecution.getId());
            response.put("status", jobExecution.getStatus());
            response.put("startTime", jobExecution.getStartTime());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error restarting job: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to restart job");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @PostMapping("/{executionId}/stop")
    public ResponseEntity<Map<String, Object>> stopJob(@PathVariable Long executionId) {
        try {
            logger.info("Received job stop request for execution ID: {}", executionId);
            
            batchJobService.stopJob(executionId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Job stop request sent successfully");
            response.put("executionId", executionId);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error stopping job: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to stop job");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/{executionId}")
    public ResponseEntity<Map<String, Object>> getJobExecution(@PathVariable Long executionId) {
        try {
            JobExecution jobExecution = batchJobService.getJobExecution(executionId);
            
            if (jobExecution == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Job execution not found");
                errorResponse.put("executionId", executionId);
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("executionId", jobExecution.getId());
            response.put("jobName", jobExecution.getJobInstance().getJobName());
            response.put("status", jobExecution.getStatus());
            response.put("startTime", jobExecution.getStartTime());
            response.put("endTime", jobExecution.getEndTime());
            response.put("exitCode", jobExecution.getExitStatus().getExitCode());
            response.put("exitMessage", jobExecution.getExitStatus().getExitDescription());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error getting job execution: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get job execution");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/{jobName}/history")
    public ResponseEntity<List<JobExecution>> getJobHistory(@PathVariable String jobName) {
        try {
            List<JobExecution> history = batchJobService.getJobHistory(jobName);
            return ResponseEntity.ok(history);
            
        } catch (Exception e) {
            logger.error("Error getting job history: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{jobName}/history/recent")
    public ResponseEntity<List<JobExecution>> getRecentJobHistory(
            @PathVariable String jobName,
            @RequestParam(defaultValue = "7") int days) {
        try {
            List<JobExecution> history = batchJobService.getRecentJobHistory(jobName, days);
            return ResponseEntity.ok(history);
            
        } catch (Exception e) {
            logger.error("Error getting recent job history: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{jobName}/statistics")
    public ResponseEntity<Map<String, Object>> getJobStatistics(@PathVariable String jobName) {
        try {
            Map<String, Object> statistics = batchJobService.getJobStatistics(jobName);
            return ResponseEntity.ok(statistics);
            
        } catch (Exception e) {
            logger.error("Error getting job statistics: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Batch Job Service");
        health.put("timestamp", java.time.LocalDateTime.now());
        
        try {
            // For now, just return basic health info
            health.put("availableJobs", 1);
            health.put("jobs", List.of("inventoryProcessingJob"));
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(health);
    }
} 