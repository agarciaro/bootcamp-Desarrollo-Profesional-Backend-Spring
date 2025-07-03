package com.bootcamp.batch.writer;

import com.bootcamp.batch.model.InventoryItem;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class InventoryItemWriter extends JdbcBatchItemWriter<InventoryItem> {
    
    @Autowired
    public InventoryItemWriter(DataSource dataSource) {
        setDataSource(dataSource);
        setSql("INSERT INTO inventory_item (product_code, product_name, category, quantity, unit_price, supplier, status, created_at, updated_at, last_updated) " +
               "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) " +
               "ON DUPLICATE KEY UPDATE " +
               "product_name = VALUES(product_name), " +
               "category = VALUES(category), " +
               "quantity = VALUES(quantity), " +
               "unit_price = VALUES(unit_price), " +
               "supplier = VALUES(supplier), " +
               "status = VALUES(status), " +
               "updated_at = CURRENT_TIMESTAMP, " +
               "last_updated = CURRENT_TIMESTAMP");
        
        setItemPreparedStatementSetter((item, ps) -> {
            ps.setString(1, item.getProductCode());
            ps.setString(2, item.getProductName());
            ps.setString(3, item.getCategory());
            ps.setInt(4, item.getQuantity());
            ps.setBigDecimal(5, item.getUnitPrice());
            ps.setString(6, item.getSupplier());
            ps.setString(7, item.getStatus().name());
        });
    }
} 