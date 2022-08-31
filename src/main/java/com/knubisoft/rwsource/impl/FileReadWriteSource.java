package com.knubisoft.rwsource.impl;

import com.knubisoft.rwsource.DataReadWriteSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@RequiredArgsConstructor
@Getter
public class FileReadWriteSource implements DataReadWriteSource<String> {
    private final File source;

    @SneakyThrows
    private static String readFileToString(File file) {
        StringBuilder fileContent = new StringBuilder();
        if (file.getName().contains(".pdf")) {
            try (PDDocument doc = PDDocument.load(file)) {
                PDFTextStripper pdfTextStripper = new PDFTextStripper();
                return pdfTextStripper.getText(doc);
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String buffer;
                while ((buffer = reader.readLine()) != null) {
                    fileContent.append(buffer).append(System.lineSeparator());
                }
            }
        }
        return fileContent.toString();
    }

    public String getContent() {
        return readFileToString(source);
    }
}
