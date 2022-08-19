package com.knubisoft;

import com.knubisoft.rwsource.DataReadWriteSource;
import lombok.SneakyThrows;

import java.util.List;

public interface ORMInterface {

    @SneakyThrows
    <T> List<T> readAll(DataReadWriteSource<?> source, Class<T> cls);
    @SneakyThrows
    <T> void writeAll(DataReadWriteSource<?> content, List<T> objects);

}