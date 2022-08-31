package com.knubisoft.writingStrategy.impl;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.knubisoft.rwsource.DataReadWriteSource;
import com.knubisoft.rwsource.impl.FileReadWriteSource;
import com.knubisoft.writingStrategy.WritingStrategy;
import lombok.SneakyThrows;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class XMLWriter implements WritingStrategy {

    @SneakyThrows
    @Override
    public <T> void writeTo(DataReadWriteSource<?> src, List<T> objects) {
        File file = ((FileReadWriteSource) src).getSource();
        XmlMapper mapper = ((XmlMapper) new XmlMapper().findAndRegisterModules());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        mapper.writerWithDefaultPrettyPrinter().withRootName("root").writeValue(file, objects);
    }
}
