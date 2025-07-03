package com.bootcamp.batch.service;

import com.bootcamp.batch.dto.InventoryItemDto;
import com.bootcamp.batch.model.InventoryItem;
import com.bootcamp.batch.model.ItemStatus;
import com.bootcamp.batch.repository.InventoryItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryItemRepository inventoryItemRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private InventoryItem testItem;
    private InventoryItemDto testItemDto;

    @BeforeEach
    void setUp() {
        testItem = new InventoryItem();
        testItem.setId(1L);
        testItem.setProductCode("TEST001");
        testItem.setProductName("Test Product");
        testItem.setCategory("Electronics");
        testItem.setQuantity(10);
        testItem.setUnitPrice(new BigDecimal("99.99"));
        testItem.setSupplier("Test Supplier");
        testItem.setStatus(ItemStatus.ACTIVE);

        testItemDto = new InventoryItemDto();
        testItemDto.setProductCode("TEST001");
        testItemDto.setProductName("Test Product");
        testItemDto.setCategory("Electronics");
        testItemDto.setQuantity(10);
        testItemDto.setUnitPrice(new BigDecimal("99.99"));
        testItemDto.setSupplier("Test Supplier");
    }

    @Test
    void getAllItems_ShouldReturnAllItems() {
        // Given
        List<InventoryItem> expectedItems = Arrays.asList(testItem);
        when(inventoryItemRepository.findAll()).thenReturn(expectedItems);

        // When
        List<InventoryItem> actualItems = inventoryService.getAllItems();

        // Then
        assertEquals(expectedItems, actualItems);
        verify(inventoryItemRepository).findAll();
    }

    @Test
    void getItemByProductCode_WhenItemExists_ShouldReturnItem() {
        // Given
        when(inventoryItemRepository.findByProductCode("TEST001")).thenReturn(Optional.of(testItem));

        // When
        Optional<InventoryItem> actualItem = inventoryService.getItemByProductCode("TEST001");

        // Then
        assertTrue(actualItem.isPresent());
        assertEquals(testItem, actualItem.get());
        verify(inventoryItemRepository).findByProductCode("TEST001");
    }

    @Test
    void getItemByProductCode_WhenItemDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(inventoryItemRepository.findByProductCode("NONEXISTENT")).thenReturn(Optional.empty());

        // When
        Optional<InventoryItem> actualItem = inventoryService.getItemByProductCode("NONEXISTENT");

        // Then
        assertFalse(actualItem.isPresent());
        verify(inventoryItemRepository).findByProductCode("NONEXISTENT");
    }

    @Test
    void createItem_WhenItemDoesNotExist_ShouldCreateItem() {
        // Given
        when(inventoryItemRepository.existsByProductCode("TEST001")).thenReturn(false);
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(testItem);

        // When
        InventoryItem createdItem = inventoryService.createItem(testItemDto);

        // Then
        assertNotNull(createdItem);
        assertEquals(testItem, createdItem);
        verify(inventoryItemRepository).existsByProductCode("TEST001");
        verify(inventoryItemRepository).save(any(InventoryItem.class));
    }

    @Test
    void createItem_WhenItemExists_ShouldThrowException() {
        // Given
        when(inventoryItemRepository.existsByProductCode("TEST001")).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.createItem(testItemDto);
        });
        verify(inventoryItemRepository).existsByProductCode("TEST001");
        verify(inventoryItemRepository, never()).save(any(InventoryItem.class));
    }

    @Test
    void updateItem_WhenItemExists_ShouldUpdateItem() {
        // Given
        when(inventoryItemRepository.findByProductCode("TEST001")).thenReturn(Optional.of(testItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(testItem);

        // When
        InventoryItem updatedItem = inventoryService.updateItem("TEST001", testItemDto);

        // Then
        assertNotNull(updatedItem);
        assertEquals(testItem, updatedItem);
        verify(inventoryItemRepository).findByProductCode("TEST001");
        verify(inventoryItemRepository).save(any(InventoryItem.class));
    }

    @Test
    void updateItem_WhenItemDoesNotExist_ShouldThrowException() {
        // Given
        when(inventoryItemRepository.findByProductCode("NONEXISTENT")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.updateItem("NONEXISTENT", testItemDto);
        });
        verify(inventoryItemRepository).findByProductCode("NONEXISTENT");
        verify(inventoryItemRepository, never()).save(any(InventoryItem.class));
    }

    @Test
    void deleteItem_WhenItemExists_ShouldDeleteItem() {
        // Given
        when(inventoryItemRepository.existsByProductCode("TEST001")).thenReturn(true);
        doNothing().when(inventoryItemRepository).deleteByProductCode("TEST001");

        // When
        inventoryService.deleteItem("TEST001");

        // Then
        verify(inventoryItemRepository).existsByProductCode("TEST001");
        verify(inventoryItemRepository).deleteByProductCode("TEST001");
    }

    @Test
    void deleteItem_WhenItemDoesNotExist_ShouldThrowException() {
        // Given
        when(inventoryItemRepository.existsByProductCode("NONEXISTENT")).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.deleteItem("NONEXISTENT");
        });
        verify(inventoryItemRepository).existsByProductCode("NONEXISTENT");
        verify(inventoryItemRepository, never()).deleteByProductCode(anyString());
    }

    @Test
    void updateStock_WhenItemExists_ShouldUpdateStock() {
        // Given
        when(inventoryItemRepository.findByProductCode("TEST001")).thenReturn(Optional.of(testItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(testItem);

        // When
        inventoryService.updateStock("TEST001", 20);

        // Then
        verify(inventoryItemRepository).findByProductCode("TEST001");
        verify(inventoryItemRepository).save(any(InventoryItem.class));
    }

    @Test
    void adjustStock_WhenAdjustmentIsValid_ShouldAdjustStock() {
        // Given
        when(inventoryItemRepository.findByProductCode("TEST001")).thenReturn(Optional.of(testItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(testItem);

        // When
        inventoryService.adjustStock("TEST001", 5);

        // Then
        verify(inventoryItemRepository).findByProductCode("TEST001");
        verify(inventoryItemRepository).save(any(InventoryItem.class));
    }

    @Test
    void adjustStock_WhenAdjustmentWouldResultInNegative_ShouldThrowException() {
        // Given
        when(inventoryItemRepository.findByProductCode("TEST001")).thenReturn(Optional.of(testItem));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.adjustStock("TEST001", -15);
        });
        verify(inventoryItemRepository).findByProductCode("TEST001");
        verify(inventoryItemRepository, never()).save(any(InventoryItem.class));
    }

    @Test
    void getLowStockItems_ShouldReturnLowStockItems() {
        // Given
        List<InventoryItem> expectedItems = Arrays.asList(testItem);
        when(inventoryItemRepository.findLowStockItems(10)).thenReturn(expectedItems);

        // When
        List<InventoryItem> actualItems = inventoryService.getLowStockItems(10);

        // Then
        assertEquals(expectedItems, actualItems);
        verify(inventoryItemRepository).findLowStockItems(10);
    }

    @Test
    void getOutOfStockItems_ShouldReturnOutOfStockItems() {
        // Given
        List<InventoryItem> expectedItems = Arrays.asList(testItem);
        when(inventoryItemRepository.findOutOfStockItems()).thenReturn(expectedItems);

        // When
        List<InventoryItem> actualItems = inventoryService.getOutOfStockItems();

        // Then
        assertEquals(expectedItems, actualItems);
        verify(inventoryItemRepository).findOutOfStockItems();
    }

    @Test
    void getTotalInventoryValue_ShouldReturnTotalValue() {
        // Given
        BigDecimal expectedValue = new BigDecimal("999.99");
        when(inventoryItemRepository.getTotalInventoryValue()).thenReturn(expectedValue);

        // When
        BigDecimal actualValue = inventoryService.getTotalInventoryValue();

        // Then
        assertEquals(expectedValue, actualValue);
        verify(inventoryItemRepository).getTotalInventoryValue();
    }

    @Test
    void getTotalInventoryValue_WhenNull_ShouldReturnZero() {
        // Given
        when(inventoryItemRepository.getTotalInventoryValue()).thenReturn(null);

        // When
        BigDecimal actualValue = inventoryService.getTotalInventoryValue();

        // Then
        assertEquals(BigDecimal.ZERO, actualValue);
        verify(inventoryItemRepository).getTotalInventoryValue();
    }
} 