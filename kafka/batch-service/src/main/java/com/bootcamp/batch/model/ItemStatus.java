package com.bootcamp.batch.model;

public enum ItemStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    DISCONTINUED("Discontinued"),
    OUT_OF_STOCK("Out of Stock"),
    LOW_STOCK("Low Stock");
    
    private final String displayName;
    
    ItemStatus(String displayName) {
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