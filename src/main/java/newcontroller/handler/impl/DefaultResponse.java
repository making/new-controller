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

import newcontroller.handler.HandlerBridge;
import newcontroller.handler.Response;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class DefaultResponse implements Response {
    private final HttpServletResponse response;
    private final List<HttpMessageConverter<?>> converters;

    private MediaType contentType;

    public DefaultResponse(HttpServletResponse response,
                           List<HttpMessageConverter<?>> converters) {
        this.response = response;
        this.converters = (converters == null ? Collections.emptyList() : converters);
    }

    public DefaultResponse(HttpServletResponse response) {
        this(response, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public HandlerBridge body(Object body) {
        HttpMessageConverter converter = HttpMessageConvertersHelper.findConverter(this.converters, body.getClass(), this.contentType);
        return new HttpMessageConverterHandlerBridge(body, converter);
    }

    @Override
    public HandlerBridge view(String viewName) {
        return new ModelAndViewHandlerBridge(viewName);
    }

    @Override
    public HandlerBridge view(View view) {
        return new ModelAndViewHandlerBridge(view);
    }

    @Override
    public HandlerBridge with(Supplier<HandlerBridge> supplier) {
        return supplier.get();
    }

    @Override
    public MediaType contentType() {
        return this.contentType;
    }

    @Override
    public Response contentType(MediaType conentType) {
        this.contentType = conentType;
        return this;
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        return clazz.cast(this.response);
    }
}
