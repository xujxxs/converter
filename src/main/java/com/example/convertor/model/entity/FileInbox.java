package com.example.convertor.model.entity;

import java.time.LocalDateTime;

import com.example.convertor.model.enums.FileInboxStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "file_inbox")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInbox {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String idempotentKey;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private LocalDateTime occuredOn;

    private LocalDateTime processedDate;

    @Column(nullable = false)
    private FileInboxStatus status;
}
