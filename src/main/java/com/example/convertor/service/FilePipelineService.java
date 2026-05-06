package com.example.convertor.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.convertor.config.S3BucketProperties;
import com.example.convertor.event.Producer;
import com.example.convertor.model.dto.FileDataDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilePipelineService {

    @Value("${queue.kafka.topic.converted-file}")
    private String SEND_TOPIC;
    private final Producer producer;
    private final S3BucketProperties s3BucketProperties;

    private final ArchiverService archiverService;
    private final ConverterService converterService;
    private final S3Service s3Service;

    public void process(String key, String event) throws Exception {
        FileDataDto donwloadedFile = s3Service.loadFormS3(s3BucketProperties.getDownloadBucketName(), event);
        archiverService.unzip(donwloadedFile).stream()
            .filter(converterService::isSupportedToConvert)
            .map(converterService::convertToPdf)
            .forEach(convertedFile -> {
                s3Service.saveFile(s3BucketProperties.getSaveBucketName(), convertedFile);
                producer.sendMessage(SEND_TOPIC, UUID.randomUUID().toString(), convertedFile.name());
            });
    }
}
