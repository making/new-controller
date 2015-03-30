package demo;

import newcontroller.RouterDefinition;
import newcontroller.RouterHandlerMapping;
import newcontroller.handler.Handler;
import newcontroller.handler.impl.DefaultHandlerApplier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerAdapter;

import javax.xml.bind.annotation.XmlRootElement;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    RouterHandlerMapping<Handler> routerHandlerMapping(HttpMessageConverters converters) {
        RouterHandlerMapping<Handler> handlerMapping = new RouterHandlerMapping<>(new DefaultHandlerApplier(converters.getConverters()));
        handlerMapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return handlerMapping;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    HandlerAdapter routerHandlerAdapter(RouterHandlerMapping<Handler> handlerMapping) {
        return handlerMapping.handlerAdapter();
    }

    @Bean
    RouterDefinition<Handler> routerDef() {
        return router -> router
                /* curl localhost:8080 ==> Sample*/
                .get("/", (req, res) -> res.body("Sample"))
                /* curl localhost:8080/hello ==> Hello World! */
                .get("/hello", (req, res) -> res.body("Hello World!"))
                /* curl localhost:8080/json ==> {"name":"John","age":30} */
                .get("/json", (req, res) -> res
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new Person("John", 30)))
                /* curl localhost:8080/xml ==> <?xml version="1.0" encoding="UTF-8" standalone="yes"?><person><age>30</age><name>John</name></person> */
                .get("/xml", (req, res) -> res
                        .contentType(MediaType.APPLICATION_XML)
                        .body(new Person("John", 30)))
                /* curl localhost:8080/template ==> [templates/hello.html will be rendered via Thymeleaf] */
                .get("/template", (req, res) -> {
                    req.put("message", "Hello World!");
                    return res.view("hello");
                })
                /* curl localhost:8080/bar/aaa ==> foo = aaa */
                .get("/bar/{foo}", (req, res) ->
                        res.body("foo = " + req.param("foo")
                                .orElse("??")))
                /* curl localhost:8080/param -d name=John ==> Hi John */
                .post("/echo", (req, res) ->
                        res.body(req.param("name")
                                .map(name -> "Hi " + name)
                                .orElse("Please input name!")))
                /* curl localhost:8080/param -d name=John -d age=30 ==> {"name":"John","age":30} */
                .post("/param", (req, res) -> {
                    Person person = req.params(Person.class);
                    return res.body(person);
                })
                /* curl localhost:8080/body -H 'Content-Type: application/json' -d '{"name":"John","age":30}' ==> {"name":"John","age":30} */
                .post("/body", (req, res) -> {
                    Person person = req.body(Person.class);
                    return res.body(person);
                });
    }

    @XmlRootElement
    public static class Person {
        private String name;
        private int age;

        public Person() {
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}