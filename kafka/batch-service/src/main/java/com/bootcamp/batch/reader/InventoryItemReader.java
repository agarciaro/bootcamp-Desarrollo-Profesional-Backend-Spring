package com.bootcamp.batch.reader;

import com.bootcamp.batch.dto.InventoryItemDto;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
public class InventoryItemReader implements ItemReader<InventoryItemDto> {
    @Override
    public InventoryItemDto read() {
        // TODO: Implement reading logic
        return null;
    }
} 