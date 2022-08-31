package com.knubisoft.parsingStrategy;

import com.knubisoft.rwsource.DataReadWriteSource;
import com.knubisoft.entity.Table;
import com.knubisoft.rwsource.impl.FileReadWriteSource;
import lombok.SneakyThrows;

public interface ParsingStrategy<T extends DataReadWriteSource> {
    Table parseToTable(T content);
}