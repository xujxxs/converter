package com.example.convertor.model.entity;

import java.time.LocalDateTime;

import com.example.convertor.model.enums.FileInboxStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "file_inbox")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInbox {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "idempotent_key", unique = true, nullable = false)
    private String idempotentKey;

    @Column(nullable = false)
    private String payload;

    @Column(name = "occured_on", nullable = false)
    private LocalDateTime occuredOn;

    private LocalDateTime processedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileInboxStatus status;

    @Override
    public boolean equals(Object anObject) {
        if(this == anObject) return true;
        if(anObject == null || getClass() != anObject.getClass()) return false;

        return ((FileInbox) anObject).getIdempotentKey().equals(this.idempotentKey);
    }

    @Override
    public int hashCode() {
        return this.idempotentKey.hashCode();
    }
}
