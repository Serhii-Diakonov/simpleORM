import lombok.SneakyThrows;
import myImpl.FileContentType;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public class ORM implements ORMInterface {

    @Override
    @SneakyThrows
    public <T> List<T> transform(DataInputSource inputSource, Class<T> cls) {
        Table table = convertToTable(inputSource);
        return convertTableToListOfClasses(table, cls);
    }

    private Table convertToTable(DataInputSource dataInputSource) {
        if (dataInputSource instanceof DatabaseInputSource) {
            return new DatabaseParsingStrategy().
                    parseToTable((DatabaseInputSource) dataInputSource);
        } else if (dataInputSource instanceof StringInputSource) {
            return getStringParsingStrategy((StringInputSource) dataInputSource).
                    parseToTable((StringInputSource) dataInputSource);
        } else {
            throw new UnsupportedOperationException("Unknown DataInputSource " + dataInputSource);
        }
    }

    private ParsingStrategy<StringInputSource> getStringParsingStrategy(StringInputSource inputSource) {
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

    private static Object transformValueToFieldType(Field field, String value) {
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

    private ParsingStrategy createStrategyByFileContentType(FileContentType FileContentType) {
        switch (FileContentType) {
            case JSON:
                return new JSONParsingStrategy();
            case XML:
                return new XMLParsingStrategy();
            case CSV:
                return new CSVParsingStrategy();
            default:
                throw new UnsupportedOperationException("Unknown strategy " + FileContentType);
        }
    }

    private FileContentType guessFileContentTypeByContent(String content) {
        char firstChar = content.charAt(0);
        switch (firstChar) {
            case '{':
            case '[':
                return FileContentType.JSON;
            case '<':
                return FileContentType.XML;
            default:
                return FileContentType.CSV;
        }
    }
}
