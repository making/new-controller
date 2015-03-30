/*
 * Copyright (C) 2015 Toshiaki Maki <makingx@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package newcontroller.handler.impl;

import newcontroller.handler.Request;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
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
        Request req = new DefaultRequest(request);
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
        Request req = new DefaultRequest(request);
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
        Request req = new DefaultRequest(request);
        Map<String, List<String>> expected = new LinkedHashMap<>();
        expected.put("foo", Arrays.asList("aaa", "bbb"));
        expected.put("bar", Arrays.asList("ccc"));
        assertThat(req.params(), is(expected));
    }

    @Test
    public void testUnwrap() throws Exception {
        HttpServletRequest request = MockMvcRequestBuilders.get("/").param("foo", "aaa")
                .buildRequest(new MockServletContext());
        Request req = new DefaultRequest(request);
        assertThat(req.unwrap(HttpServletRequest.class), is(sameInstance(request)));
    }

    @Test
    public void testPopulateFromParams() throws Exception {
        HttpServletRequest request = MockMvcRequestBuilders.get("/")
                .param("foo", "aaa")
                .param("bar", "100")
                .buildRequest(new MockServletContext());
        Request req = new DefaultRequest(request);
        Foo foo = req.params(Foo.class);
        assertThat(foo.getFoo(), is("aaa"));
        assertThat(foo.getBar(), is(100));
    }

    @Test
    public void testPopulateCollection() throws Exception {
        HttpServletRequest request = MockMvcRequestBuilders.get("/")
                .param("text", "aaa")
                .param("vars", "a")
                .param("vars", "b")
                .param("vars", "c")
                .buildRequest(new MockServletContext());
        Request req = new DefaultRequest(request);
        Bar bar = req.params(Bar.class);
        assertThat(bar.getText(), is("aaa"));
        assertThat(bar.getVars(), is(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void testPopulateMap() throws Exception {
        HttpServletRequest request = MockMvcRequestBuilders.get("/")
                .param("attr[aaa]", "aaa")
                .param("attr[bbb]", "bbb")
                .param("attr[ccc]", "ccc")
                .buildRequest(new MockServletContext());
        Request req = new DefaultRequest(request);
        Piyo piyo = req.params(Piyo.class);
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("aaa", "aaa");
        expected.put("bbb", "bbb");
        expected.put("ccc", "ccc");
        assertThat(piyo.getAttr(), is(expected));
    }

    @Test
    public void testPopulateFromBody() throws Exception {
        HttpServletRequest request = MockMvcRequestBuilders.post("/")
                .content("{\"foo\":\"aaa\",\"bar\":100}")
                .contentType(MediaType.APPLICATION_JSON)
                .buildRequest(new MockServletContext());
        Request req = new DefaultRequest(request, Arrays.asList(new GsonHttpMessageConverter()));
        Foo foo = req.body(Foo.class);
        assertThat(foo.getFoo(), is("aaa"));
        assertThat(foo.getBar(), is(100));
    }

    @Test
    public void testPutAndGet() throws Exception {
        HttpServletRequest request = MockMvcRequestBuilders.get("/")
                .buildRequest(new MockServletContext());
        Request req = new DefaultRequest(request);
        LocalDate now = LocalDate.now();
        req
                .put("foo", 100)
                .put("bar", "hoge")
                .put("piyo", now);

        assertThat(req.get("foo", Integer.class), is(100));
        assertThat(req.get("bar", String.class), is("hoge"));
        assertThat(req.get("piyo", LocalDate.class), is(now));
    }

    @Test
    public void testModel() throws Exception {
        HttpServletRequest request = MockMvcRequestBuilders.get("/")
                .buildRequest(new MockServletContext());
        Request req = new DefaultRequest(request);
        LocalDate now = LocalDate.now();
        req
                .put("foo", 100)
                .put("bar", "hoge")
                .put("piyo", now);
        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put("foo", 100);
        expected.put("bar", "hoge");
        expected.put("piyo", now);
        assertThat(req.model(), is(expected));
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

    public static class Bar {
        private String text;
        private List<String> vars;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<String> getVars() {
            return vars;
        }

        public void setVars(List<String> vars) {
            this.vars = vars;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Bar{");
            sb.append("text='").append(text).append('\'');
            sb.append(", vars=").append(vars);
            sb.append('}');
            return sb.toString();
        }
    }

    public static class Piyo {
        private Map<String, String> attr;


        public Piyo() {
        }

        public Piyo(Map<String, String> attr) {
            this.attr = attr;
        }

        public Map<String, String> getAttr() {
            return attr;
        }

        public void setAttr(Map<String, String> attr) {
            this.attr = attr;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Piyo{");
            sb.append("attr=").append(attr);
            sb.append('}');
            return sb.toString();
        }
    }
}
