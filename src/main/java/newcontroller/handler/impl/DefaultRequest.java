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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.WebDataBinder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultRequest implements Request {
    private final HttpServletRequest request;
    private final List<HttpMessageConverter<?>> converters;

    public DefaultRequest(HttpServletRequest request, List<HttpMessageConverter<?>> converters) {
        this.request = request;
        this.converters = (converters == null ? Collections.emptyList() : converters);
    }

    public DefaultRequest(HttpServletRequest request) {
        this(request, null);
    }

    @Override
    public Optional<String> param(String name) {
        return Optional.ofNullable(this.request.getParameter(name));
    }

    @Override
    public List<String> params(String name) {
        String[] values = this.request.getParameterValues(name);
        return values == null ? Collections.emptyList() : Arrays.asList(values);
    }

    @Override
    public Map<String, List<String>> params() {
        return this.request.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, x -> Arrays.asList(x.getValue())));
    }

    @Override
    public <T> T params(Class<T> clazz) {
        T obj = BeanUtils.instantiate(clazz);
        WebDataBinder binder = new WebDataBinder(obj);
        binder.bind(new MutablePropertyValues(this.request.getParameterMap()));
        return obj;
    }

    @Override
    public <T> T body(Class<T> clazz) {
        MediaType mediaType = MediaType.parseMediaType(this.request.getContentType());
        HttpMessageConverter converter = HttpMessageConvertersHelper.findConverter(this.converters, clazz, mediaType);
        try {
            return clazz.cast(converter.read(clazz, new ServletServerHttpRequest(this.request)));
        } catch (IOException e) {
            throw new UncheckedIOException(e); // TODO
        }
    }

    @Override
    public Request put(String key, Object value) {
        this.request.setAttribute(key, value);
        return this;
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(this.request.getAttribute(key));
    }

    @Override
    public Map<String, ?> model() {
        return Collections.list(this.request.getAttributeNames())
                .stream()
                .collect(Collectors.toMap(Function.identity(), this.request::getAttribute));
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        return clazz.cast(this.request);
    }
}
