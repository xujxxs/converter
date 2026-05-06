package com.example.convertor.util.archiver;

import java.util.List;

import com.example.convertor.model.dto.FileDataDto;

public interface Archiver {
    List<FileDataDto> unzip(FileDataDto file2Unzip);
}
