package com.example.convertor.util.archiver;

import java.util.List;

import com.example.convertor.model.dto.File;

public interface Archiver {
    List<File> unzip(File file2Unzip);
}
