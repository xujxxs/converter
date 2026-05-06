package com.example.convertor.util.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.example.convertor.exception.ConverterException;
import com.example.convertor.model.dto.FileDataDto;

public class ConverterTxt implements Converter {

    public FileDataDto convertToPdf(FileDataDto file2Convent) {
        return new FileDataDto(
            file2Convent.name().replace(".txt", ".pdf"),
            "pdf",
            fileConvertLogic(file2Convent.bytes()));
    }

    private byte[] fileConvertLogic(byte[] fileBytes) {
        String text = new String(fileBytes, StandardCharsets.UTF_8);

        try (PDDocument doc = new PDDocument();
            ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.setLeading(14.5f);
                cs.newLineAtOffset(50, 750);

                for (String line : text.split("\\r?\\n")) {
                    cs.showText(line);
                    cs.newLine();
                }
                cs.endText();
            }

            doc.save(baos);
            return baos.toByteArray();
        } catch(IOException e) {
            throw new ConverterException("Txt can't be converted: " + e.getMessage());
        }
    }
}
