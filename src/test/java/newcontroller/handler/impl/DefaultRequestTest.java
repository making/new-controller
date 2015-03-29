package newcontroller.handler.impl;

import org.junit.Test;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class DefaultRequestTest {

    @Test
    public void testParam() throws Exception {
        HttpServletRequest request = MockMvcRequestBuilders.get("/").param("foo", "aaa")
                .buildRequest(new MockServletContext());
        DefaultRequest req = new DefaultRequest(request);
        assertThat(req.param("foo").isPresent(), is(true));
        assertThat(req.param("foo").get(), is("aaa"));
        assertThat(req.param("bar").isPresent(), is(false));
    }

    @Test
    public void testParams() throws Exception {
        HttpServletRequest request = MockMvcRequestBuilders.get("/")
                .param("foo", "aaa")
                .param("foo", "bbb")
                .buildRequest(new MockServletContext());
        DefaultRequest req = new DefaultRequest(request);
        assertThat(req.params("foo"), is(Arrays.asList("aaa", "bbb")));
        assertThat(req.params("bar"), is(Arrays.asList()));
    }

    @Test
    public void testParams1() throws Exception {
        HttpServletRequest request = MockMvcRequestBuilders.get("/")
                .param("foo", "aaa")
                .param("foo", "bbb")
                .param("bar", "ccc")
                .buildRequest(new MockServletContext());
        DefaultRequest req = new DefaultRequest(request);
        Map<String, List<String>> expected = new LinkedHashMap<>();
        expected.put("foo", Arrays.asList("aaa", "bbb"));
        expected.put("bar", Arrays.asList("ccc"));
        assertThat(req.params(), is(expected));
    }

    @Test
    public void testUnwrap() throws Exception {
        HttpServletRequest request = MockMvcRequestBuilders.get("/").param("foo", "aaa")
                .buildRequest(new MockServletContext());
        DefaultRequest req = new DefaultRequest(request);
        assertThat(req.unwrap(HttpServletRequest.class), is(sameInstance(request)));
    }

    @Test
    public void testPopulate() throws Exception {
        HttpServletRequest request = MockMvcRequestBuilders.get("/")
                .param("foo", "aaa")
                .param("bar", "100")
                .buildRequest(new MockServletContext());
        DefaultRequest req = new DefaultRequest(request);
        Foo foo = req.populate(Foo.class);
        assertThat(foo.getFoo(), is("aaa"));
        assertThat(foo.getBar(), is(100));
    }


    public static class Foo {
        private String foo;
        private int bar;

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }

        public int getBar() {
            return bar;
        }

        public void setBar(int bar) {
            this.bar = bar;
        }

        @Override
        public String toString() {
            return "Foo{" +
                    "foo='" + foo + '\'' +
                    ", bar=" + bar +
                    '}';
        }
    }
}
