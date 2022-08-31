package com.knubisoft.writingStrategy.impl;

import com.knubisoft.rwsource.DataReadWriteSource;
import com.knubisoft.writingStrategy.WritingStrategy;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBWriter implements WritingStrategy {

    Connection destSrc;
    Map<Type, String> typeToType = new HashMap<>();

    public DBWriter() {
        typeMapInit();
    }

    @Override
    public <T> void writeTo(DataReadWriteSource<?> src, List<T> objects) {
        destSrc = ((Connection) src.getSource());
        createTable(objects.get(0));
        writeToTable(objects, objects.get(0).getClass().getSimpleName().toLowerCase());
    }

    @SneakyThrows
    private <T> void writeToTable(List<T> objects, String tableName) {
        StringBuilder insertQuery;
        for (T object : objects) {
            Field[] fields = object.getClass().getDeclaredFields();
            insertQuery = new StringBuilder();
            insertQuery.append("INSERT INTO ").append(tableName).append(" (");
            for (Field field : fields) {
                insertQuery.append(field.getName()).append(", ");
            }
            insertQuery.replace(insertQuery.lastIndexOf(", "), insertQuery.lastIndexOf(", ") + 2, ") values (");
            for (Field field : fields) {
                Object extractedValue = getValueFromField(object, field);
                insertQuery.append(extractedValue).append(", ");
            }
            insertQuery.replace(insertQuery.lastIndexOf(", "), insertQuery.lastIndexOf(", ") + 2, ");");
            executeQueries(insertQuery.toString());
        }
    }

    @SneakyThrows
    private <T> Object getValueFromField(T dataObject, Field field) {
        field.setAccessible(true);
        for (Method m : dataObject.getClass().getDeclaredMethods()) {
            if (m.getName().equalsIgnoreCase("get" + field.getName())) {
                if (field.getType() == String.class) {
                    return "'" + m.invoke(dataObject) + "'";
                } else return m.invoke(dataObject);
            }
        }
        return null;
    }

    private void typeMapInit() {
        typeToType.put(String.class, "VARCHAR(300)");
        typeToType.put(int.class, "INTEGER");
        typeToType.put(Integer.class, "INTEGER");
        typeToType.put(float.class, "FLOAT");
        typeToType.put(Float.class, "FLOAT");
        typeToType.put(double.class, "DOUBLE");
        typeToType.put(Double.class, "DOUBLE");
        typeToType.put(LocalDate.class, "DATE");
        typeToType.put(LocalDateTime.class, "DATETIME");
        typeToType.put(long.class, "BIGINT");
        typeToType.put(Long.class, "BIGINT");
        typeToType.put(BigInteger.class, "BIGINT");
    }


    private <T> void createTable(T object) {
        String dropQuery = "DROP TABLE IF EXISTS " + object.getClass().getSimpleName().toLowerCase() + ";";
        StringBuilder createQuery = new StringBuilder();
        createQuery = createQuery.append("CREATE TABLE ").append(object.getClass().getSimpleName().toLowerCase()).append(" (id INTEGER PRIMARY KEY AUTOINCREMENT,");
        for (Field field : object.getClass().getDeclaredFields()) {
            createQuery.append(field.getName()).append(" ").append(typeToType.get(field.getType())).append(",");
        }
        createQuery.replace(createQuery.lastIndexOf(","), createQuery.lastIndexOf(",") + 1, ")");
        executeQueries(dropQuery, createQuery.toString());
    }

    @SneakyThrows
    private void executeQueries(String... queries) {
        for (String query : queries) {
            Statement statement = destSrc.createStatement();
            statement.executeUpdate(query);
        }
    }
}
