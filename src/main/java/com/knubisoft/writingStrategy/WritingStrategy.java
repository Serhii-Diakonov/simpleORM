package com.knubisoft.writingStrategy;

import com.knubisoft.rwsource.DataReadWriteSource;

import java.io.File;
import java.util.List;

public interface WritingStrategy {
    <T> void writeTo(DataReadWriteSource<?> src, List<T> objects);
}
