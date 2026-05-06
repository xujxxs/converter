package com.example.convertor.util.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.example.convertor.exception.ConverterException;
import com.example.convertor.model.dto.FileDataDto;

public class ConverterJpg implements Converter {

    public FileDataDto convertToPdf(FileDataDto file2Convent) {
        return new FileDataDto(
            file2Convent.name().replace(".jpg", ".pdf"),
            "pdf",
            fileConvertLogic(file2Convent.bytes()));
    }

    private byte[] fileConvertLogic(byte[] fileBytes) {
        try (PDDocument doc = new PDDocument();
            InputStream is = new ByteArrayInputStream(fileBytes);
            ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            doc.addPage(page);

            BufferedImage bufferedImage = ImageIO.read(is);
            if (bufferedImage == null) {
                throw new IOException("Не удалось декодировать изображение. Проверьте формат файла.");
            }
            PDImageXObject pdImage = LosslessFactory.createFromImage(doc, bufferedImage);

            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                content.drawImage(pdImage, 0, 0,
                        page.getMediaBox().getWidth(),
                        page.getMediaBox().getHeight());
            }

            doc.save(baos);
            return baos.toByteArray();
        } catch(IOException e) {
            throw new ConverterException("Jpg can't be converted: " + e.getMessage());
        }
    }
}
