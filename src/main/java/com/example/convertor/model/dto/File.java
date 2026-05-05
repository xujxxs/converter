package com.example.convertor.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class File {
    private String name;
    private String extension;
    private byte[] bytes;
}
