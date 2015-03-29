package lambda.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Map;

public class CapturedHttpServletRequestAdopter extends HttpServletRequestWrapper implements CapturedHttpServletRequest {
    private final Map<String, String> captured;
    private final HttpServletRequest request;

    public CapturedHttpServletRequestAdopter(Map<String, String> captured, HttpServletRequest request) {
        super(request);
        this.captured = captured;
        this.request = request;
    }

    @Override
    public String getParameter(String name) {
        String captured = this.captured.get(name);
        return captured == null ? super.getParameter(name) : captured;
    }

    @Override
    public Map<String, String> pathParams() {
        return this.captured;
    }
}
