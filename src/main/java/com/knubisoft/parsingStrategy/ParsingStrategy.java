package com.knubisoft.parsingStrategy;

import com.knubisoft.rwsource.DataReadWriteSource;
import com.knubisoft.entity.Table;

public interface ParsingStrategy<T extends DataReadWriteSource> {
    Table parseToTable(T content);
}