package com.example.convertor.util.converter;

import com.example.convertor.model.dto.FileDataDto;

public interface Converter {
    FileDataDto convertToPdf(FileDataDto file2Convert);
}
