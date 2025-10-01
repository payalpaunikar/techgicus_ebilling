package com.example.techgicus_ebilling.techgicus_ebilling.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class S3ClientConfig {

    @Value("${do.spaces.access-key}")
    private String accessKey;

    @Value("${do.space.secret-key}")
    private String secretKey;

    @Value("${do.space.endpoint}")
    private String endpoint;

    @Bean
    public S3Client s3Client(){
        return S3Client.builder()
                .region(Region.US_EAST_1) // This is required but will be ignored by the custom endpoint
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                        accessKey,
                        secretKey)))
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .build();
    }
}
