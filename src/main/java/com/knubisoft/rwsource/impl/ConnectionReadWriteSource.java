package com.knubisoft.rwsource.impl;

import com.knubisoft.rwsource.DataReadWriteSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RequiredArgsConstructor
@Getter
@Setter
public class ConnectionReadWriteSource implements DataReadWriteSource<ResultSet> {

    private Connection source;
    private String tableName;

    public ConnectionReadWriteSource(Connection source) {
        this.source = source;
    }

    @Override
    @SneakyThrows
    public ResultSet getContent() {
        Statement statement = source.createStatement();
        return statement.executeQuery("SELECT * FROM " + tableName);
    }

    public Connection getSource() {
        return source;
    }
}
