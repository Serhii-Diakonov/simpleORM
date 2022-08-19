package com.knubisoft.writingStrategy.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.knubisoft.writingStrategy.WritingStrategy;
import lombok.SneakyThrows;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class XMLWriter implements WritingStrategy {

    @SneakyThrows
    @Override
    public <T> void writeTo(File file, List<T> objects) {
        XmlMapper mapper = ((XmlMapper) new XmlMapper().findAndRegisterModules());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        mapper.writerWithDefaultPrettyPrinter().withRootName("root").writeValue(file, objects);
    }
}
