package com.example.convertor.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.convertor.model.dto.FileDataDto;
import com.example.convertor.util.converter.Converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConverterService {

    private final Map<String, Converter> classesConverters;

    public FileDataDto convertToPdf(FileDataDto file2Convert) {
        return classesConverters.get(file2Convert.extensions()).convertToPdf(file2Convert);
    }

    public boolean isSupportedToConvert(FileDataDto file2Check) {
        log.debug("Ext: {}", file2Check.extensions());
        return classesConverters.containsKey(file2Check.extensions());
    }
}
