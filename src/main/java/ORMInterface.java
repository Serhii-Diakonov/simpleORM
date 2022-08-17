import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.List;

public interface ORMInterface {
    @SneakyThrows
    <T> List<T> transform(DataInputSource content, Class<T> cls);

    interface DataInputSource {
    }

    @RequiredArgsConstructor
    @Getter
    final class StringInputSource implements DataInputSource {
        private final String content;
    }

    @RequiredArgsConstructor
    @Getter
    final class DatabaseInputSource implements DataInputSource {
        private final ResultSet resultSet;
    }

}
