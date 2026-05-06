package com.example.convertor.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.springframework.stereotype.Service;

import com.example.convertor.model.dto.FileDataDto;
import com.example.convertor.util.archiver.Archiver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArchiverService {

    private final Map<String, Archiver> classesArhivers;

    public List<FileDataDto> unzip(FileDataDto file2Unzip) {
        List<FileDataDto> unzipedFiles = new ArrayList<>();
        Queue<FileDataDto> files2Unzip = new LinkedList<>();
        files2Unzip.add(file2Unzip);

        while (!files2Unzip.isEmpty()) {
            FileDataDto fileFormQueue = files2Unzip.poll();
            log.debug("Start unpack: {}", fileFormQueue.name());
            if(!classesArhivers.containsKey(fileFormQueue.extensions())) {
                unzipedFiles.add(fileFormQueue);
                continue;
            }

            files2Unzip.addAll(classesArhivers.get(fileFormQueue.extensions())
                .unzip(fileFormQueue));
            log.debug("Files need to check on unzip: {}", files2Unzip.size());
        }

        if(log.isDebugEnabled()) {
            log.debug("Unziped files: {}", 
                unzipedFiles.stream().map(FileDataDto::name).limit(50).toList());
        }

        return unzipedFiles;
    }
}
