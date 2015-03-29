package lambda.support;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface CapturedHttpServletRequest extends HttpServletRequest {
    Map<String, String> pathParams();
}
