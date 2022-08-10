import java.io.File;
import java.util.List;

public interface TransformStrategy {
    <T> List<T> transform(File file, Class<T> cls);
}
