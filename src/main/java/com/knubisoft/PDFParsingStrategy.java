package com.knubisoft;

import com.knubisoft.anno.Data;
import com.knubisoft.anno.Lookup;
import com.knubisoft.entity.Table;
import com.knubisoft.parsingStrategy.ParsingStrategy;
import com.knubisoft.rwsource.impl.FileReadWriteSource;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFParsingStrategy implements ParsingStrategy<FileReadWriteSource> {

    private Class<?> cls;

    public PDFParsingStrategy(Class<?> cls) {
        this.cls = cls;
    }

    @Override
    @SneakyThrows
    public Table parseToTable(FileReadWriteSource content) {
        String buffer = content.getContent();
        if (cls.isAnnotationPresent(Data.class)) {
            List<Object> result = new ArrayList<>();
            Map<String, String> regex = findFieldNameRegex();
            String commonRegex = String.join(" ", regex.values());
            Pattern pattern = Pattern.compile(commonRegex);
            Matcher matcher = pattern.matcher(buffer);
            while (matcher.find())
                result.add(getEntity(regex, matcher));
        }
        return null;
    }

    private Map<String, String> findFieldNameRegex() {
        Map<String, String> res = new LinkedHashMap<>();
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(Lookup.class)) {
                res.put(field.getName(), field.getAnnotation(Lookup.class).regex());
            }
        }
        return res;
    }

    @SneakyThrows
    private Object getEntity(Map<String, String> regex, Matcher matcher) {
        Object instance = cls.getConstructor().newInstance();
        for (String s : regex.keySet()) {
            Field field = cls.getDeclaredField(s);
            field.setAccessible(true);
            field.set(instance, matcher.group(s).replaceAll("\\h", " "));
        }
        return instance;
    }
}
