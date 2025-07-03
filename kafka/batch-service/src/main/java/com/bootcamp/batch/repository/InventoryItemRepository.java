package com.bootcamp.batch.repository;

import com.bootcamp.batch.model.InventoryItem;
import com.bootcamp.batch.model.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    
    Optional<InventoryItem> findByProductCode(String productCode);
    
    List<InventoryItem> findByCategory(String category);
    
    List<InventoryItem> findByStatus(ItemStatus status);
    
    List<InventoryItem> findByQuantityLessThanEqual(Integer quantity);
    
    List<InventoryItem> findByQuantityGreaterThan(Integer quantity);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity <= :threshold")
    List<InventoryItem> findLowStockItems(@Param("threshold") Integer threshold);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity = 0")
    List<InventoryItem> findOutOfStockItems();
    
    @Query("SELECT i FROM InventoryItem i WHERE i.unitPrice > :minPrice")
    List<InventoryItem> findExpensiveItems(@Param("minPrice") BigDecimal minPrice);
    
    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.category = :category")
    Long countByCategory(@Param("category") String category);
    
    @Query("SELECT SUM(i.quantity * i.unitPrice) FROM InventoryItem i")
    BigDecimal getTotalInventoryValue();
    
    @Query("SELECT SUM(i.quantity * i.unitPrice) FROM InventoryItem i WHERE i.category = :category")
    BigDecimal getTotalInventoryValueByCategory(@Param("category") String category);
    
    @Query("SELECT i.supplier, COUNT(i) FROM InventoryItem i GROUP BY i.supplier")
    List<Object[]> getItemCountBySupplier();
    
    @Query("SELECT i.category, AVG(i.unitPrice) FROM InventoryItem i GROUP BY i.category")
    List<Object[]> getAveragePriceByCategory();
    
    boolean existsByProductCode(String productCode);
    
    void deleteByProductCode(String productCode);
    
    @Query("SELECT i FROM InventoryItem i WHERE i.lastUpdated < :date")
    List<InventoryItem> findItemsNotUpdatedSince(@Param("date") java.time.LocalDateTime date);
} 