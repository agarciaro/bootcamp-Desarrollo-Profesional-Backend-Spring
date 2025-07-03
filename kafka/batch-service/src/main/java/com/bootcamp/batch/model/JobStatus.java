package com.bootcamp.batch.model;

public enum JobStatus {
    STARTING("Starting"),
    RUNNING("Running"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    STOPPED("Stopped"),
    ABANDONED("Abandoned");
    
    private final String displayName;
    
    JobStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
} 