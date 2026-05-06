package com.example.convertor.service;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import com.example.convertor.model.dto.FileDataDto;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    public FileDataDto loadFormS3(String bucket, String key) throws IOException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
            .build();

        return new FileDataDto(
            key, 
            FilenameUtils.getExtension(key), 
            s3Client.getObject(request).readAllBytes());
    }

    public String saveFile(String bucketName, FileDataDto file) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(file.name())
            .build();
        RequestBody body = RequestBody.fromBytes(file.bytes());
        s3Client.putObject(request, body);
        return file.name();
    }
}
