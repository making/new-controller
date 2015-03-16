package lambda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public class RouterHandlerMapping extends AbstractHandlerMapping {
    @Autowired
    Optional<List<RouterDefinition>> routerDefinitions;
    private final Logger log = LoggerFactory.getLogger(RouterHandlerMapping.class);
    private final Router router = new Router();

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        if (request.getRequestURI().endsWith(".ico")) {
            return null;
        }

        String path = request.getRequestURI();
        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        return router.lookup(path, method);
    }

    @PostConstruct
    public void init() {
        routerDefinitions.ifPresent(defs -> {
            for (RouterDefinition def : defs) {
                def.define(router);
            }
        });
        router.handlerMap.forEach((k, v) -> {
            log.info("Path:{}, Method:{}, Handler:{}", k.getPath(), k.getMethod(), v);
        });
    }
}
