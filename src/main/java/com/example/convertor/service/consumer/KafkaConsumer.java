package com.example.convertor.service.consumer;

import java.time.LocalDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

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
public class KafkaConsumer {

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
            filePipelineService.process(idempotentionId, event);
            fileInboxService.endProcess(fileInbox, FileInboxStatus.COMPLETED);
            log.info("File: {}, was converted", event);
        } catch(Exception e) {
            fileInboxService.endProcess(fileInbox, FileInboxStatus.ERROR);
            log.error("Error with message: {}. Detailed:", idempotentionId, e);
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
