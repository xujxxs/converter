package com.example.convertor.service;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import com.example.convertor.model.dto.File;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    public File loadFormS3(String bucket, String key) throws IOException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
            .build();

        return File.builder()
                .name(key)
                .extension(FilenameUtils.getExtension(key))
                .bytes(s3Client.getObject(request).readAllBytes())
            .build();
    }

    public String saveFile(String bucketName, File file) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getName())
            .build();
        RequestBody body = RequestBody.fromBytes(file.getBytes());
        s3Client.putObject(request, body);
        return file.getName();
    }
}
