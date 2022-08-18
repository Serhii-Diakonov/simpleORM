package com.knubisoft;

import com.knubisoft.anno.TableAnno;
import com.knubisoft.entity.Table;
import com.knubisoft.parsingStrategy.ParsingStrategy;
import com.knubisoft.parsingStrategy.impl.CSVParsingStrategy;
import com.knubisoft.parsingStrategy.impl.JSONParsingStrategy;
import com.knubisoft.parsingStrategy.impl.XMLParsingStrategy;
import com.knubisoft.rwsource.impl.ConnectionReadWriteSource;
import com.knubisoft.rwsource.DataReadWriteSource;
import com.knubisoft.parsingStrategy.impl.DatabaseParsingStrategy;
import com.knubisoft.rwsource.impl.FileReadWriteSource;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ORM implements ORMInterface {

    @Override
    @SneakyThrows
    public <T> List<T> readAll(DataReadWriteSource<?> source, Class<T> cls) {
        Table table = convertToTable(source, cls);
        return convertTableToListOfClasses(table, cls);
    }

    private <T> List<T> convertTableToListOfClasses(Table table, Class<T> cls) {
        List<T> result = new ArrayList<>();
        for (int index = 0; index < table.size(); index++) {
            Map<String, String> row = table.getTableRowByIndex(index);
            T instance = reflectTableRowToClass(row, cls);
            result.add(instance);
        }
        return result;
    }

    @SneakyThrows
    private <T> T reflectTableRowToClass(Map<String, String> row, Class<T> cls) {
        T instance = cls.getDeclaredConstructor().newInstance();
        for (Field each : cls.getDeclaredFields()) {
            each.setAccessible(true);
            String value = row.get(each.getName());
            if (value != null) {
                each.set(instance, transformValueToFieldType(each, value));
            }
        }
        return instance;
    }

    private Object transformValueToFieldType(Field field, String value) {
        Map<Class<?>, Function<String, Object>> typeToFunction = new LinkedHashMap<>();
        typeToFunction.put(String.class, s -> s);
        typeToFunction.put(int.class, Integer::parseInt);
        typeToFunction.put(Integer.class, Integer::parseInt);
        typeToFunction.put(float.class, Float::parseFloat);
        typeToFunction.put(Float.class, Float::parseFloat);
        typeToFunction.put(double.class, Double::parseDouble);
        typeToFunction.put(Double.class, Double::parseDouble);
        typeToFunction.put(LocalDate.class, LocalDate::parse);
        typeToFunction.put(LocalDateTime.class, LocalDate::parse);
        typeToFunction.put(long.class, Long::parseLong);
        typeToFunction.put(Long.class, Long::parseLong);
        typeToFunction.put(BigInteger.class, BigInteger::new);

        return typeToFunction.getOrDefault(field.getType(), type -> {
            throw new UnsupportedOperationException("Type is not supported by parser " + field.getType());
        }).apply(value);
    }

    private Table convertToTable(DataReadWriteSource dataInputSource, Class<?> cls) {
        if (dataInputSource instanceof ConnectionReadWriteSource) {
            ConnectionReadWriteSource dbSrc = ((ConnectionReadWriteSource) dataInputSource);
            return new DatabaseParsingStrategy(cls).parseToTable(dbSrc);
        } else if (dataInputSource instanceof FileReadWriteSource) {
            FileReadWriteSource fileSrc = ((FileReadWriteSource) dataInputSource);
            return getStringParsingStrategy(fileSrc).parseToTable(fileSrc);
        } else {
            throw new UnsupportedOperationException("Unknown DataInputSource " + dataInputSource);
        }
    }

    private ParsingStrategy<FileReadWriteSource> getStringParsingStrategy(FileReadWriteSource inputSource) {
        String content = inputSource.getContent();
        char firstChar = content.charAt(0);
        switch (firstChar) {
            case '{':
            case '[':
                return new JSONParsingStrategy();
            case '<':
                return new XMLParsingStrategy();
            default:
                return new CSVParsingStrategy();
        }
    }
}
