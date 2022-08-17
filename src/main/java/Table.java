import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class Table {
    private final Map<Integer, Map<String, String>> table;

    // X Y Z
    // 1 2 3
    // 4 5 6
    // 7 8 9

    public String getCell(int row, String columnName) {
        Map<String, String> nameToCell = table.get(row);
        if (nameToCell != null) {
            return nameToCell.get(columnName);
        }
        return null;
    }

    int size() {
        return table.size();
    }

    Map<String, String> getTableRowByIndex(int row) {
        Map<String, String> result = table.get(row);
        return result == null ? null : new LinkedHashMap<>(result);
    }
}
