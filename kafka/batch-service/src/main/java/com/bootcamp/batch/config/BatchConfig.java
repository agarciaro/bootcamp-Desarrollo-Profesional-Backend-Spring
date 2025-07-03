package com.bootcamp.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.bootcamp.batch.dto.InventoryItemDto;
import com.bootcamp.batch.listener.InventoryJobExecutionListener;
import com.bootcamp.batch.listener.InventoryStepExecutionListener;
import com.bootcamp.batch.model.InventoryItem;
import com.bootcamp.batch.processor.InventoryItemProcessor;
import com.bootcamp.batch.tasklet.InventoryAlertTasklet;
import com.bootcamp.batch.tasklet.NotificationTasklet;
import com.bootcamp.batch.writer.InventoryItemWriter;

@Configuration
public class BatchConfig {
    
    @Value("${batch.job.inventory.chunk-size:100}")
    private int chunkSize;
    
    @Value("${batch.job.inventory.retry-limit:3}")
    private int retryLimit;
    
    @Value("${batch.job.inventory.skip-limit:10}")
    private int skipLimit;
    
    @Value("${batch.job.inventory.input-file}")
    private String inputFile;
    
    @Value("${batch.job.inventory.output-file}")
    private String outputFile;
    
    @Value("${batch.job.inventory.error-file}")
    private String errorFile;
    
    // Job Configuration
    @Bean
    public Job inventoryProcessingJob(JobRepository jobRepository, 
                                    InventoryJobExecutionListener jobListener,
                                    Step validateAndProcessStep,
                                    Step generateAlertsStep,
                                    Step sendNotificationsStep) {
        return new JobBuilder("inventoryProcessingJob", jobRepository)
                .listener(jobListener)
                .start(validateAndProcessStep)
                .next(generateAlertsStep)
                .next(sendNotificationsStep)
                .build();
    }
    
    // Step 1: Validate and Process Inventory Items
    @Bean
    public Step validateAndProcessStep(JobRepository jobRepository,
                                     PlatformTransactionManager transactionManager,
                                     InventoryStepExecutionListener stepListener,
                                     DataSource dataSource) {
        return new StepBuilder("validateAndProcessStep", jobRepository)
                .<InventoryItemDto, InventoryItem>chunk(chunkSize, transactionManager)
                .reader(inventoryItemReader())
                .processor(inventoryItemProcessor())
                .writer(inventoryItemWriter(dataSource))
                .listener(stepListener)
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(retryLimit)
                .skip(Exception.class)
                .skipLimit(skipLimit)
                .build();
    }
    
    // Step 2: Generate Alerts for Low Stock Items
    @Bean
    public Step generateAlertsStep(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager) {
        return new StepBuilder("generateAlertsStep", jobRepository)
                .tasklet(inventoryAlertTasklet(), transactionManager)
                .build();
    }
    
    // Step 3: Send Notifications
    @Bean
    public Step sendNotificationsStep(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager) {
        return new StepBuilder("sendNotificationsStep", jobRepository)
                .tasklet(notificationTasklet(), transactionManager)
                .build();
    }
    
    // Reader Configuration
    @Bean
    public ItemReader<InventoryItemDto> inventoryItemReader() {
        FlatFileItemReader<InventoryItemDto> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(inputFile));
        reader.setLinesToSkip(1); // Skip header
        
        DefaultLineMapper<InventoryItemDto> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("productCode", "productName", "category", "quantity", "unitPrice", "supplier", "status");
        tokenizer.setDelimiter(",");
        
        BeanWrapperFieldSetMapper<InventoryItemDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(InventoryItemDto.class);
        
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        
        reader.setLineMapper(lineMapper);
        return reader;
    }
    
    // Processor Configuration
    @Bean
    public ItemProcessor<InventoryItemDto, InventoryItem> inventoryItemProcessor() {
        return new InventoryItemProcessor();
    }
    
    // Writer Configuration
    @Bean
    public ItemWriter<InventoryItem> inventoryItemWriter(DataSource dataSource) {
        return new InventoryItemWriter(dataSource);
    }
    
    // Tasklet Beans
    @Bean
    public InventoryAlertTasklet inventoryAlertTasklet() {
        return new InventoryAlertTasklet();
    }
    
    @Bean
    public NotificationTasklet notificationTasklet() {
        return new NotificationTasklet();
    }
    
    // Listener Beans
    @Bean
    public InventoryJobExecutionListener inventoryJobExecutionListener() {
        return new InventoryJobExecutionListener();
    }
    
    @Bean
    public InventoryStepExecutionListener inventoryStepExecutionListener() {
        return new InventoryStepExecutionListener();
    }
} 