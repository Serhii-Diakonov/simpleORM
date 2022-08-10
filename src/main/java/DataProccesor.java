import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

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

public class DataProccesor {

    private static TransformStrategy transformStrategy;

    private static FileContentType resolveFileContentType(File file) {
        if (file.getName().endsWith(".csv")) {
            return FileContentType.CSV;
        } else if (file.getName().endsWith(".json")) {
            return FileContentType.JSON;
        } else if (file.getName().endsWith(".xml")) {
            return FileContentType.XML;
        } else return FileContentType.UNKNOWN;
    }


    public static <T> List<T> transform(File file, Class<T> cls) {
        switch (resolveFileContentType(file)) {
            case CSV -> transformStrategy = new CsvTransform();
            case XML -> transformStrategy = new XmlTransform();
            case JSON -> transformStrategy = new JsonTransform();
            default -> throw new RuntimeException("Unknown file content type. Must be XML, JSON or CSV");
        }

        return transformStrategy.transform(file, cls);
    }
}
