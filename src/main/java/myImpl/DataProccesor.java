package myImpl;

import java.io.*;
import java.util.List;

public class DataProccesor {

    private static FileContentType resolveFileContentType(File file) {
        if (file.getName().endsWith(".csv")) {
            return FileContentType.CSV;
        } else if (file.getName().endsWith(".json")) {
            return FileContentType.JSON;
        } else if (file.getName().endsWith(".xml")) {
            return FileContentType.XML;
        } else throw new UnsupportedOperationException("Unknown strategy ");
    }


    public static <T> List<T> transform(File file, Class<T> cls) {
        TransformStrategy transformStrategy;

        switch (resolveFileContentType(file)) {
            case CSV -> transformStrategy = new CsvTransform();
            case XML -> transformStrategy = new XmlTransform();
            case JSON -> transformStrategy = new JsonTransform();
            default -> throw new RuntimeException("Unknown file content type. Must be XML, JSON or CSV");
        }

        return transformStrategy.transform(file, cls);
    }
}
