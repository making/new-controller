package demo;

import lambda.RouterDefinition;
import lambda.RouterHandlerMapping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.servlet.HandlerAdapter;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    RouterHandlerMapping<HttpRequestHandler> routerHandlerMapping() {
        RouterHandlerMapping<HttpRequestHandler> handlerMapping = new RouterHandlerMapping<>(
                (handler, request, response) -> {
                    handler.handleRequest(request, response);
                    return null;
                });
        handlerMapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return handlerMapping;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    HandlerAdapter routerHandlerAdapter(RouterHandlerMapping<HttpRequestHandler> handlerMapping) {
        return handlerMapping.handlerAdapter();
    }

    @Bean
    RouterDefinition<HttpRequestHandler> routerDef() {
        return router -> router
                .get("/", (req, res) -> {
                    res.getWriter().print("Sample");
                    res.getWriter().flush();
                })
                .get("/hello", (req, res) -> {
                    res.getWriter().print("Hello World!");
                    res.getWriter().flush();
                })
                .get("/bar/{foo}", (req, res) -> {
                    res.getWriter().print("foo = " + req.getParameter("foo"));
                    res.getWriter().flush();
                })
                .post("/echo", (req, res) -> {
                    res.getWriter().print("Hi " + req.getParameter("name"));
                    res.getWriter().flush();
                });
    }
}