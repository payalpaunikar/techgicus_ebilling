package com.example.techgicus_ebilling.techgicus_ebilling.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {

    private final S3Client s3Client;

    public FileService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Value("${do.space.bucket-name}")
    private String bucketName;

    @Value("${do.space.endpoint}")
    private String endpoint;

    private final String spaceUrl =  "https://resallingo.sfo3.digitaloceanspaces.com/resallingo/";


    //Method to upload a file
    public String uploadFile(MultipartFile file) throws IOException {

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        String uniqueFileName = UUID.randomUUID().toString()+"-"+originalFileName;

        // Define folder structure inside the bucket
        String folderPath = "upload/ebilling/images/";
        String fullPath = folderPath + uniqueFileName;

        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Create temporary file
        Path tempFile = Files.createTempFile("upload-", uniqueFileName);
        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        // Upload to DigitalOcean Spaces
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fullPath) // Use folder structure in key
                .acl("public-read")
                .build();

        s3Client.putObject(putObjectRequest, tempFile);

        // Clean up temporary file
        Files.deleteIfExists(tempFile);

        // Return the public URL of the uploaded file
        return spaceUrl + fullPath;

    }
}
