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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NotificationTasklet implements Tasklet {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationTasklet.class);
    
    @Autowired
    private InventoryItemRepository inventoryItemRepository;
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    @Value("${kafka.topics.inventory-notifications:inventory-notifications}")
    private String notificationTopic;
    
    @Value("${batch.job.inventory.low-stock-threshold:10}")
    private int lowStockThreshold;
    
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Starting inventory notification sending...");
        
        try {
            // Get inventory summary
            List<InventoryItem> allItems = inventoryItemRepository.findByStatus(ItemStatus.ACTIVE);
            List<InventoryItem> lowStockItems = allItems.stream()
                    .filter(item -> item.getQuantity() < lowStockThreshold)
                    .collect(Collectors.toList());
            List<InventoryItem> outOfStockItems = inventoryItemRepository.findOutOfStockItems();
            
            // Calculate total inventory value
            BigDecimal totalValue = allItems.stream()
                    .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // Create summary notification
            Map<String, Object> summaryNotification = new HashMap<>();
            summaryNotification.put("type", "summary");
            summaryNotification.put("timestamp", LocalDateTime.now());
            summaryNotification.put("totalItems", allItems.size());
            summaryNotification.put("lowStockItems", lowStockItems.size());
            summaryNotification.put("outOfStockItems", outOfStockItems.size());
            summaryNotification.put("totalValue", totalValue);
            
            // Send summary notification
            kafkaTemplate.send(notificationTopic, "summary", summaryNotification);
            logger.info("Sent summary notification: {} items, {} low stock, {} out of stock, total value: {}", 
                       allItems.size(), lowStockItems.size(), outOfStockItems.size(), totalValue);
            
            // Send low stock notifications
            for (InventoryItem item : lowStockItems) {
                Map<String, Object> lowStockNotification = new HashMap<>();
                lowStockNotification.put("type", "low-stock");
                lowStockNotification.put("timestamp", LocalDateTime.now());
                lowStockNotification.put("productCode", item.getProductCode());
                lowStockNotification.put("productName", item.getProductName());
                lowStockNotification.put("quantity", item.getQuantity());
                lowStockNotification.put("threshold", lowStockThreshold);
                lowStockNotification.put("unitPrice", item.getUnitPrice());
                lowStockNotification.put("supplier", item.getSupplier());
                
                kafkaTemplate.send(notificationTopic, "low-stock", lowStockNotification);
                logger.info("Sent low stock notification for: {} - {} (Quantity: {})", 
                           item.getProductCode(), item.getProductName(), item.getQuantity());
            }
            
            // Send out of stock notifications
            for (InventoryItem item : outOfStockItems) {
                Map<String, Object> outOfStockNotification = new HashMap<>();
                outOfStockNotification.put("type", "out-of-stock");
                outOfStockNotification.put("timestamp", LocalDateTime.now());
                outOfStockNotification.put("productCode", item.getProductCode());
                outOfStockNotification.put("productName", item.getProductName());
                outOfStockNotification.put("unitPrice", item.getUnitPrice());
                outOfStockNotification.put("supplier", item.getSupplier());
                
                kafkaTemplate.send(notificationTopic, "out-of-stock", outOfStockNotification);
                logger.info("Sent out of stock notification for: {} - {}", 
                           item.getProductCode(), item.getProductName());
            }
            
            // Update contribution statistics
            contribution.incrementReadCount();
            contribution.incrementWriteCount(1 + lowStockItems.size() + outOfStockItems.size());
            
            logger.info("Inventory notification sending completed successfully");
            
        } catch (Exception e) {
            logger.error("Error sending inventory notifications: {}", e.getMessage(), e);
            throw e;
        }
        
        return RepeatStatus.FINISHED;
    }
} 