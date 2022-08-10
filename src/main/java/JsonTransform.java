import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonTransform implements TransformStrategy{
    @Override
    public <T> List<T> transform(File file, Class<T> cls) {
        List<T> personList;
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        try {
            ObjectReader objectReader = mapper.readerForListOf(Person.class);
            personList = objectReader.readValue(file);
            /*
            //Manual parsing
            ArrayNode root = (ArrayNode) mapper.readTree(new File(fileName));
            for (int i = 0; i < root.size(); i++) {
                personList.add(new Person(root.get(i).get("name").asText(),
                        LocalDate.parse(root.get(i).get("dateOfBirth").asText()),
                        root.get(i).get("age").asInt(),
                        root.get(i).get("position").asText(),
                        root.get(i).get("salary").asDouble(),
                        (float) root.get(i).get("xxx").asDouble()));
            }*/
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return personList;
    }
}
