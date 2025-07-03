package com.bootcamp.batch.writer;

import com.bootcamp.batch.model.InventoryItem;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class InventoryItemWriter implements ItemWriter<InventoryItem> {
    @Override
    public void write(Chunk<? extends InventoryItem> items) {
        // TODO: Implement writing logic
    }
} 