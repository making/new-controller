package lambda;

import org.springframework.http.HttpMethod;
import org.springframework.web.HttpRequestHandler;

import java.util.concurrent.ConcurrentHashMap;

public class Router {

    final ConcurrentHashMap<PathAndMethod, HttpRequestHandler> handlerMap = new ConcurrentHashMap();

    public Router get(String path, HttpRequestHandler handler) {
        this.handlerMap.put(new PathAndMethod(path, HttpMethod.GET), handler);
        return this;
    }

    public Router post(String path, HttpRequestHandler handler) {
        this.handlerMap.put(new PathAndMethod(path, HttpMethod.POST), handler);
        return this;
    }

    public HttpRequestHandler lookup(String path, HttpMethod method) {
        // Poor lookup strategy because of prototype ;)
        return this.handlerMap.get(new PathAndMethod(path, method));
    }

    public static class PathAndMethod {
        private final String path;
        private final HttpMethod method;

        public PathAndMethod(String path, HttpMethod method) {
            this.path = path;
            this.method = method;
        }

        public String getPath() {
            return path;
        }

        public HttpMethod getMethod() {
            return method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PathAndMethod)) return false;

            PathAndMethod that = (PathAndMethod) o;

            if (method != that.method) return false;
            if (path != null ? !path.equals(that.path) : that.path != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = path != null ? path.hashCode() : 0;
            result = 31 * result + (method != null ? method.hashCode() : 0);
            return result;
        }
    }
}
