package com.bootcamp.batch.reader;

import com.bootcamp.batch.dto.InventoryItemDto;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class InventoryItemReader extends FlatFileItemReader<InventoryItemDto> {
    
    @Value("${spring.batch.job.inventory.input-file}")
    private String inputFile;
    
    public InventoryItemReader() {
        setLineMapper(createLineMapper());
        setLinesToSkip(1); // Skip header
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // Set the resource based on the input file path
        Resource resource;
        if (inputFile.startsWith("classpath:")) {
            resource = new ClassPathResource(inputFile.replace("classpath:", ""));
        } else {
            resource = new FileSystemResource(inputFile);
        }
        setResource(resource);
        super.afterPropertiesSet();
    }
    
    private DefaultLineMapper<InventoryItemDto> createLineMapper() {
        DefaultLineMapper<InventoryItemDto> lineMapper = new DefaultLineMapper<>();
        
        // Configure the tokenizer
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("productCode", "productName", "category", "quantity", "unitPrice", "supplier", "status");
        lineMapper.setLineTokenizer(tokenizer);
        
        // Configure the field set mapper
        BeanWrapperFieldSetMapper<InventoryItemDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(InventoryItemDto.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        
        return lineMapper;
    }
} 