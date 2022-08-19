package com.knubisoft.writingStrategy;

import java.io.File;
import java.util.List;

public interface WritingStrategy {
    <T> void writeTo(File file, List<T> objects);
}
