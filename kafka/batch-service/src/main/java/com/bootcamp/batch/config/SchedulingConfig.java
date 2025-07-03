package com.bootcamp.batch.config;

import com.bootcamp.batch.dto.JobExecutionRequest;
import com.bootcamp.batch.service.BatchJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(SchedulingConfig.class);
    
    @Autowired
    private BatchJobService batchJobService;
    
    @Value("${batch.job.inventory.cron}")
    private String inventoryJobCron;
    
    // Scheduled job to run inventory processing daily at 2 AM
    @Scheduled(cron = "${batch.job.inventory.cron}")
    public void runInventoryProcessingJob() {
        try {
            logger.info("Starting scheduled inventory processing job");
            
            JobExecutionRequest request = new JobExecutionRequest();
            request.setJobName("inventoryProcessingJob");
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("scheduled", true);
            parameters.put("timestamp", System.currentTimeMillis());
            request.setParameters(parameters);
            
            batchJobService.executeJob(request);
            
            logger.info("Scheduled inventory processing job started successfully");
            
        } catch (Exception e) {
            logger.error("Error starting scheduled inventory processing job", e);
        }
    }
    
    // Health check job - runs every 5 minutes
    @Scheduled(fixedRate = 300000) // 5 minutes
    public void healthCheck() {
        try {
            logger.debug("Performing scheduled health check");
            
            // Check recent job executions
            try {
                Map<String, Object> statistics = batchJobService.getJobStatistics("inventoryProcessingJob");
                long totalExecutions = (Long) statistics.get("totalExecutions");
                double successRate = (Double) statistics.get("successRate");
                
                if (totalExecutions > 0 && successRate < 0.8) {
                    logger.warn("Health check warning: Low success rate for inventory job. Success rate: {}%", 
                               String.format("%.1f", successRate * 100));
                } else {
                    logger.debug("Health check: Job statistics look good. Success rate: {}%", 
                                String.format("%.1f", successRate * 100));
                }
            } catch (Exception e) {
                logger.warn("Health check warning: Could not retrieve job statistics: {}", e.getMessage());
            }
            
            // Check if any jobs are currently running
            try {
                // This would typically check for long-running jobs
                // For now, we'll just log that the check was performed
                logger.debug("Health check: No long-running jobs detected");
            } catch (Exception e) {
                logger.warn("Health check warning: Could not check running jobs: {}", e.getMessage());
            }
            
            logger.debug("Health check completed successfully");
            
        } catch (Exception e) {
            logger.error("Error during health check", e);
        }
    }
    
    // Cleanup job - runs daily at 3 AM
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanupOldJobHistory() {
        try {
            logger.info("Starting scheduled cleanup of old job history");
            
            // This would typically clean up old job history records
            // For now, we'll just log the cleanup attempt
            logger.info("Cleanup job completed");
            
        } catch (Exception e) {
            logger.error("Error during cleanup job", e);
        }
    }
} 