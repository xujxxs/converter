package com.example.convertor.util.archiver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FilenameUtils;

import com.example.convertor.exception.ArchiverException;
import com.example.convertor.model.dto.FileDataDto;

public class ArchiverZip implements Archiver {

    public List<FileDataDto> unzip(FileDataDto file2Unzip) {
        try {
            return fileUnzipLogic(file2Unzip);
        } catch(Exception e) {
            throw new ArchiverException("Zip can't be unziped: " + e.getMessage());
        }
    }

    private List<FileDataDto> fileUnzipLogic(FileDataDto file2Unzip) throws IOException {
        String fullPathFile = FilenameUtils.getFullPath(file2Unzip.name());
        List<FileDataDto> result = new ArrayList<>();
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(file2Unzip.bytes()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) continue;
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    baos.write(buffer, 0, len);
                }
                result.add(new FileDataDto(
                    fullPathFile + entry.getName(),
                    FilenameUtils.getExtension(entry.getName()),
                    baos.toByteArray()));
                zis.closeEntry();
            }
        }
        return result;
    }
}
