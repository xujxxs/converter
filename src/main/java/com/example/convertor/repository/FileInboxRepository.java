package com.example.convertor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.convertor.model.entity.FileInbox;

public interface FileInboxRepository extends JpaRepository<FileInbox, Long> {

    Optional<FileInbox> findByIdempotentKey(String idempotentKey);
    void deleteByIdempotentKey(String idempotentKey);
}
