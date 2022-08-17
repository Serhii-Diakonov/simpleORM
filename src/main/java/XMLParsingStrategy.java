import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class XMLParsingStrategy implements ParsingStrategy {
    @SneakyThrows
    @Override
    public Table parseToTable(String content) {
        XmlMapper mapper = new XmlMapper();
        ObjectNode tree = ((ObjectNode) mapper.readTree(content));
        Map<Integer, Map<String, String>> res = buildTable(tree);
        return new Table(res);
    }

    private Map<Integer, Map<String, String>> buildTable(JsonNode tree) {
        Map<Integer, Map<String, String>> map = new LinkedHashMap<>();
        Iterator<JsonNode> iterator = tree.get(tree.fieldNames().next()).iterator();
        int index = 0;
        while(iterator.hasNext()){
            map.put(index++, buildRow(iterator.next()));
        }
        return map;
    }

    private Map<String, String> buildRow(JsonNode node) {
        Map<String, String> row = new LinkedHashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()){
            Map.Entry<String, JsonNode> field = fields.next();
            row.put(field.getKey(), field.getValue().textValue());
        }
        return row;
    }
}