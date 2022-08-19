package com.knubisoft.writingStrategy.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knubisoft.writingStrategy.WritingStrategy;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.List;

public class JSONWriter implements WritingStrategy {
    @Override
    @SneakyThrows
    public <T> void writeTo(File file, List<T> objects) {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
//        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, objects);
    }
}
