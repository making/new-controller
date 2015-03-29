package newcontroller.handler;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Request {
    Optional<String> param(String name);

    List<String> params(String name);

    Map<String, List<String>> params();

    <T> T populate(Class<T> clazz);

    <T> T unwrap(Class<T> clazz);
}
