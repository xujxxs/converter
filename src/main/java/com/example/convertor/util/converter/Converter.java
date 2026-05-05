package com.example.convertor.util.converter;

import com.example.convertor.model.dto.File;

public interface Converter {
    File convertToPdf(File file2Convert);
}
