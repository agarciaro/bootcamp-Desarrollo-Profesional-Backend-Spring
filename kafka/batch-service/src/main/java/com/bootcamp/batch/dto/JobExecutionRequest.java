package com.bootcamp.batch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class JobExecutionRequest {
    
    @NotBlank(message = "Job name is required")
    private String jobName;
    
    @NotNull(message = "Job parameters are required")
    private Map<String, Object> parameters;
    
    private boolean restart = false;
    
    private Long previousExecutionId;
    
    // Constructors
    public JobExecutionRequest() {}
    
    public JobExecutionRequest(String jobName, Map<String, Object> parameters) {
        this.jobName = jobName;
        this.parameters = parameters;
    }
    
    // Getters and Setters
    public String getJobName() {
        return jobName;
    }
    
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    
    public Map<String, Object> getParameters() {
        return parameters;
    }
    
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
    
    public boolean isRestart() {
        return restart;
    }
    
    public void setRestart(boolean restart) {
        this.restart = restart;
    }
    
    public Long getPreviousExecutionId() {
        return previousExecutionId;
    }
    
    public void setPreviousExecutionId(Long previousExecutionId) {
        this.previousExecutionId = previousExecutionId;
    }
    
    @Override
    public String toString() {
        return "JobExecutionRequest{" +
                "jobName='" + jobName + '\'' +
                ", parameters=" + parameters +
                ", restart=" + restart +
                ", previousExecutionId=" + previousExecutionId +
                '}';
    }
} 