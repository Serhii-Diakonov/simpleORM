package com.knubisoft.parsingStrategy.impl;

import com.knubisoft.anno.TableAnno;
import com.knubisoft.entity.Table;
import com.knubisoft.parsingStrategy.ParsingStrategy;
import com.knubisoft.rwsource.impl.ConnectionReadWriteSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DatabaseParsingStrategy implements ParsingStrategy<ConnectionReadWriteSource> {
    private final Class<?> cls;
    @Override
    public Table parseToTable(ConnectionReadWriteSource src) {
        setTableNameFromClass(src);

        ResultSet rs = src.getContent();
        Map<Integer, Map<String, String>> result = buildTable(rs);
        return new Table(result);
    }

    private void setTableNameFromClass(ConnectionReadWriteSource src) {
        TableAnno anno = cls.getAnnotation(TableAnno.class);
        if (anno != null) {
            String tableName;
            if (anno.tableName().isEmpty()) {
                tableName = cls.getSimpleName().toLowerCase();
            } else {
                tableName = anno.tableName();
            }
            src.setTable(tableName);
        } else throw new RuntimeException("Cannot find appropriate table for class " + cls.getSimpleName());
    }

    @SneakyThrows
    private Map<Integer, Map<String, String>> buildTable(ResultSet rs) {
        ResultSetMetaData metadata = rs.getMetaData();

        Map<Integer, Map<String, String>> result = new LinkedHashMap<>();
        int rowId = 0;
        while (rs.next()) {
            Map<String, String> row = new LinkedHashMap<>();
            for (int index = 1; index <= metadata.getColumnCount(); index++) {
                row.put(metadata.getColumnName(index), rs.getString(index));
            }
            result.put(rowId, row);
            rowId++;
        }
        return result;
    }
}