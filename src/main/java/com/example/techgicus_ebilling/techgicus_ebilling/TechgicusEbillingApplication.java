package com.example.techgicus_ebilling.techgicus_ebilling;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
// In Spring Boot 3.x, you SHOULD NOT use @EnableBatchProcessing because if i added it's mean
// "Okay, the developer is configuring Batch manually → I won't do any auto-configuration."
//As a result, these properties are completely ignored:
//propertiesspring.batch.jdbc.initialize-schema=always   ← Does NOTHING
//spring.batch.job.enabled=false               ← Does NOTHING
//@EnableBatchProcessing
@EnableAsync
public class TechgicusEbillingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechgicusEbillingApplication.class, args);
	}

}



