package myImpl;

import myImpl.TransformStrategy;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvTransform implements TransformStrategy {
    public static final String CSV_DELIMITER = ",";
    public static final String CSV_COMMENT = "--";

    @Override
    public <T> List<T> transform(File file, Class<T> cls) {
        List<String> lines = readFile(file);
        Map<Integer, String> mapping = buildMapping(lines.get(0));

        return lines.subList(1, lines.size()).stream().map(line -> toType(line, cls, mapping)).collect(Collectors.toList());
    }

    private Map<Integer, String> buildMapping(String headerLine) {
        Map<Integer, String> map = new LinkedHashMap<>();
        String[] columnNames = headerLine.split(CSV_DELIMITER);
        for (int i = 0; i < columnNames.length; i++) {
            String val = columnNames[i];
            if (val.contains(CSV_COMMENT)) {
                val = val.split(CSV_COMMENT)[0];
            }
            map.put(i, val.trim().toLowerCase());
        }
        return map;
    }

    private <T> T toType(String line, Class<T> cls, Map<Integer, String> mapping) {
        T type = null;
        try {
            type = cls.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String[] array = line.split(CSV_DELIMITER);
        for (int i = 0; i < array.length; i++) {
            String val = array[i];
            String fieldName = mapping.get(i);
            setValueIntoFieldOrThrow(val, fieldName, type);
        }
        return type;
    }

    private void setValueIntoFieldOrThrow(String val, String fieldName, Object type) {
        try {
            Field field = null;
            for (Field f : type.getClass().getDeclaredFields()) {
                if (f.getName().toLowerCase().equals(fieldName)) {
                    field = f;
                    break;
                }
            }
            field.setAccessible(true);
            field.set(type, transformValueToFieldType(field, val));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object transformValueToFieldType(Field field, String val) {
        Map<Class<?>, Function<String, Object>> typeToFunction = new LinkedHashMap<>();
        typeToFunction.put(String.class, s -> s);
        typeToFunction.put(int.class, Integer::parseInt);
        typeToFunction.put(Float.class, Float::parseFloat);
        typeToFunction.put(LocalDate.class, LocalDate::parse);
        typeToFunction.put(LocalDateTime.class, LocalDate::parse);
        typeToFunction.put(Long.class, Long::parseLong);
        typeToFunction.put(BigInteger.class, BigInteger::new);
        typeToFunction.put(double.class, Double::parseDouble);

        return typeToFunction.getOrDefault(field.getType(), type -> {
            throw new UnsupportedOperationException("Type is not supported by parser " + type);
        }).apply(val);
    }

    private List<String> readFile(File file) {
        List<String> fileContent = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                fileContent.add(buffer);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileContent;
    }
}
