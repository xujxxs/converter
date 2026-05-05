package com.example.convertor.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.springframework.stereotype.Service;

import com.example.convertor.model.dto.File;
import com.example.convertor.util.archiver.Archiver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArchiverService {

    private final Map<String, Archiver> classesArhivers;

    public List<File> unzip(File file2Unzip) {
        List<File> unzipedFiles = new ArrayList<>();
        Queue<File> files2Unzip = new LinkedList<>();
        files2Unzip.add(file2Unzip);

        while (!files2Unzip.isEmpty()) {
            File fileFormQueue = files2Unzip.poll();
            log.debug("Start unpack: {}", fileFormQueue.getName());
            if(!classesArhivers.containsKey(fileFormQueue.getExtension())) {
                unzipedFiles.add(fileFormQueue);
                continue;
            }

            files2Unzip.addAll(classesArhivers.get(fileFormQueue.getExtension())
                .unzip(fileFormQueue));
            log.debug("Files need to check on unzip: {}", files2Unzip.size());
        }

        if(log.isDebugEnabled()) {
            log.debug("Unziped files: {}", 
                unzipedFiles.stream().map(File::getName).limit(50).toList());
        }

        return unzipedFiles;
    }
}
