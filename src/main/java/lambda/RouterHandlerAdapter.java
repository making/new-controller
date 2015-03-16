package lambda;

import org.springframework.web.HttpRequestHandler;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RouterHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object o) {
        return (o instanceof HttpRequestHandler);
    }

    @Override
    public ModelAndView handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpRequestHandler.class.cast(o).handleRequest(httpServletRequest, httpServletResponse);
        return null;
    }

    @Override
    public long getLastModified(HttpServletRequest httpServletRequest, Object o) {
        return -1;
    }
}
