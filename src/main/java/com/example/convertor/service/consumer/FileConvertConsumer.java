package com.example.convertor.service.consumer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.example.convertor.event.Producer;
import com.example.convertor.model.entity.FileInbox;
import com.example.convertor.model.enums.FileInboxStatus;
import com.example.convertor.service.FileInboxService;
import com.example.convertor.service.FilePipelineService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileConvertConsumer {

    @Value("${queue.kafka.topic.end-convert.success}")
    private String endSuccessTopicName;
    @Value("${queue.kafka.topic.end-convert.success}")
    private String endErrorTopicName;

    private final Producer producer;
    private final FileInboxService fileInboxService;
    private final FilePipelineService filePipelineService;

    @KafkaListener(topics = "${queue.kafka.topic.file-convert}", groupId = "${queue.kafka.group-id}")
    public void converterListener(String event, @Header(KafkaHeaders.RECEIVED_KEY) @NonNull String idempotentionId) {
        
        log.info("Create converter event consumed {}", event);
        if(fileInboxService.findByToken(idempotentionId).isPresent()) {
            log.warn("Inbox not created, {} already exist", idempotentionId);
            return;
        }
        FileInbox fileInbox = fileInboxService.create(createFileInbox(event, idempotentionId));

        try {
            List<String> fileKeys = filePipelineService.process(idempotentionId, event);
            producer.sendMessage(
                endSuccessTopicName, 
                idempotentionId, 
                fileKeys.toString());

            fileInboxService.updateStatus(fileInbox, FileInboxStatus.COMPLETED);
            log.info("File: {}, was converted", event);
        } catch(Exception e) {
            producer.sendMessage(
                endErrorTopicName, 
                idempotentionId, 
                e.getMessage());

            fileInboxService.updateStatus(fileInbox, FileInboxStatus.ERROR);
            log.error("Error with message: {}. Detailed: ", idempotentionId, e);
        }
    }

    private FileInbox createFileInbox(String event, String idempotentKey) {
        return FileInbox.builder()
                .idempotentKey(idempotentKey)
                .payload(event)
                .occuredOn(LocalDateTime.now())
                .status(FileInboxStatus.PROCESSED)
            .build();
    }
}
