package demo;

import lambda.RouterDefinition;
import lambda.RouterHandlerAdapter;
import lambda.RouterHandlerMapping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    HandlerMapping routerHandlerMapping() {
        RouterHandlerMapping handlerMapping = new RouterHandlerMapping();
        handlerMapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return handlerMapping;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    HandlerAdapter routerHandlerAdapter() {
        return new RouterHandlerAdapter();
    }

    @Bean
    RouterDefinition routerDef() {
        return router -> {
            router.get("/", (req, res) -> {
                res.getWriter().println("Sample");
                res.getWriter().flush();
            });
            router.get("/hello", (req, res) -> {
                res.getWriter().println("Hello World!");
                res.getWriter().flush();
            });
        };
    }
}