package myImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import myImpl.TransformStrategy;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class XmlTransform implements TransformStrategy {
    @Override
    public <T> List<T> transform(File file, Class<T> cls) {
        List<T> objectList;
        XmlMapper xmlMapper = (XmlMapper) new XmlMapper().findAndRegisterModules();
        try {
            ObjectReader objectReader = xmlMapper.readerForListOf(cls);
            objectList = objectReader.readValue(file);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return objectList;
    }
}
