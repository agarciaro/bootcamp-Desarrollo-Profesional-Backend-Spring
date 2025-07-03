package com.bootcamp.batch.tasklet;

import com.bootcamp.batch.model.InventoryItem;
import com.bootcamp.batch.model.ItemStatus;
import com.bootcamp.batch.repository.InventoryItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventoryAlertTasklet implements Tasklet {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryAlertTasklet.class);
    
    @Autowired
    private InventoryItemRepository inventoryItemRepository;
    
    @Value("${batch.job.inventory.low-stock-threshold:10}")
    private int lowStockThreshold;
    
    @Value("${batch.job.inventory.expensive-threshold:100.0}")
    private BigDecimal expensiveThreshold;
    
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Starting inventory alert generation...");
        
        try {
            // Find low stock items
            List<InventoryItem> allItems = inventoryItemRepository.findByStatus(ItemStatus.ACTIVE);
            List<InventoryItem> lowStockItems = allItems.stream()
                    .filter(item -> item.getQuantity() < lowStockThreshold)
                    .collect(Collectors.toList());
            
            // Find out of stock items
            List<InventoryItem> outOfStockItems = inventoryItemRepository.findOutOfStockItems();
            
            // Find expensive items
            List<InventoryItem> expensiveItems = inventoryItemRepository.findExpensiveItems(expensiveThreshold);
            
            // Log alerts
            if (!lowStockItems.isEmpty()) {
                logger.warn("Found {} items with low stock (threshold: {})", 
                           lowStockItems.size(), lowStockThreshold);
                lowStockItems.forEach(item -> 
                    logger.warn("Low stock alert: {} - {} (Quantity: {})", 
                               item.getProductCode(), item.getProductName(), item.getQuantity())
                );
            }
            
            if (!outOfStockItems.isEmpty()) {
                logger.error("Found {} items out of stock", outOfStockItems.size());
                outOfStockItems.forEach(item -> 
                    logger.error("Out of stock alert: {} - {}", 
                               item.getProductCode(), item.getProductName())
                );
            }
            
            if (!expensiveItems.isEmpty()) {
                logger.info("Found {} expensive items (threshold: {})", 
                           expensiveItems.size(), expensiveThreshold);
                expensiveItems.forEach(item -> 
                    logger.info("Expensive item: {} - {} (Price: {})", 
                               item.getProductCode(), item.getProductName(), item.getUnitPrice())
                );
            }
            
            // Update contribution statistics
            contribution.incrementReadCount();
            contribution.incrementWriteCount(lowStockItems.size() + outOfStockItems.size() + expensiveItems.size());
            
            logger.info("Inventory alert generation completed successfully");
            
        } catch (Exception e) {
            logger.error("Error generating inventory alerts: {}", e.getMessage(), e);
            throw e;
        }
        
        return RepeatStatus.FINISHED;
    }
} 