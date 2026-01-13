package com.example.techgicus_ebilling.techgicus_ebilling.batch;


import com.example.techgicus_ebilling.techgicus_ebilling.batch.dto.ExcelRowData;
import com.example.techgicus_ebilling.techgicus_ebilling.batch.processor.ExcelItemProcessor;
import com.example.techgicus_ebilling.techgicus_ebilling.batch.reader.ZipExcelItemReader;
import com.example.techgicus_ebilling.techgicus_ebilling.batch.writer.DatabaseItemWriter;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Configuration
public class BatchConfig {

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PurchaseItemRepository purchaseItemRepository;




    // This is the job name we used in ImportService
    @Bean
    public Job importExcelDataJob(JobRepository jobRepository, Step testStep) {
        return new JobBuilder("importExcelDataJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(testStep)
                .build();
    }

    @Bean
    public Step importDataStep(JobRepository jobRepository,
                               PlatformTransactionManager transactionManager,
                               ZipExcelItemReader reader,
                               ExcelItemProcessor processor,
                               DatabaseItemWriter writer) {
        return new StepBuilder("importDataStep", jobRepository)
                .<ExcelRowData, Object>chunk(50, transactionManager)  // Process 50 rows at a time
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }



//    // Temporary simple step to confirm everything works
//    @Bean
//    public Step testStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        return new StepBuilder("testStep", jobRepository)
//                .tasklet((contribution, chunkContext) -> {
//                    Map<String, Object> jobParams = chunkContext.getStepContext().getJobParameters();
//                    String archivePath = (String) jobParams.get("archiveFilePath");
//                    String archiveType = (String) jobParams.get("archiveType");
//                    System.out.println(">>> Spring Batch Job STARTED successfully!");
//                    System.out.println(">>> Archive file location: " + archivePath);
//                    System.out.println(">>> Archive type: " + archiveType);
//
//                    if (archivePath == null || archivePath.isEmpty()) {
//                        System.err.println("ERROR: archiveFilePath parameter is NULL or missing!");
//                        return RepeatStatus.FINISHED;
//                    }
//
//                    Path path = Paths.get(archivePath);
//                    // Optional: Check if file exists
//                    if (Files.exists(path)) {
//                        System.out.println(">>> FILE FOUND AND ACCESSIBLE! Ready for processing.");
//                        System.out.println(">>> Size: " + Files.size(path) + " bytes");
//                    } else {
//                        System.out.println(">>> WARNING: File NOT found at: " + archivePath);
//                    }
//
//                    return RepeatStatus.FINISHED;
//                }, transactionManager)
//                .build();
//    }
}
