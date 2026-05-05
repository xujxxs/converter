package com.example.convertor.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.convertor.model.entity.FileInbox;
import com.example.convertor.model.enums.FileInboxStatus;
import com.example.convertor.repository.FileInboxRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileInboxService {

    private final FileInboxRepository fileInboxRepository;

    public FileInbox create(FileInbox fileInbox) {
        return fileInboxRepository.save(fileInbox);
    }

    public Optional<FileInbox> findByToken(String idempotentId) {
        return fileInboxRepository.findByIdempotentKey(idempotentId);
    }
    
    public FileInbox endProcess(FileInbox fileInbox, FileInboxStatus status) {
        fileInbox.setProcessedDate(LocalDateTime.now());
        fileInbox.setStatus(status);
        return fileInboxRepository.save(fileInbox);
    }

    public void deleteByToken(String idempotentId) {
        fileInboxRepository.deleteByIdempotentKey(idempotentId);
    }
}
