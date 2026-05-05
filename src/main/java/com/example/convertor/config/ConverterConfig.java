package com.example.convertor.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.convertor.util.archiver.Archiver;
import com.example.convertor.util.archiver.ArchiverZip;
import com.example.convertor.util.converter.Converter;
import com.example.convertor.util.converter.ConverterJpg;
import com.example.convertor.util.converter.ConverterPng;
import com.example.convertor.util.converter.ConverterTxt;

@Configuration
public class ConverterConfig {

    @Bean
    Map<String, Archiver> arhivers() {
        Map<String, Archiver> archivers = new HashMap<>();
        archivers.put("zip", new ArchiverZip());
        return archivers;
    }

    @Bean
    Map<String, Converter> converters() {
        Map<String, Converter> converters = new HashMap<>();
        converters.put("txt", new ConverterTxt());
        converters.put("jpg", new ConverterJpg());
        converters.put("png", new ConverterPng());
        return converters;
    }
}
