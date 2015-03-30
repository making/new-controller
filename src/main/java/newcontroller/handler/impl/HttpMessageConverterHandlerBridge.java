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
import newcontroller.handler.Request;
import newcontroller.handler.Response;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpMessageConverterHandlerBridge<T> implements HandlerBridge<Void> {
    private final T body;
    private final HttpMessageConverter<T> messageConverter;

    public HttpMessageConverterHandlerBridge(T body, HttpMessageConverter<T> messageConverter) {
        this.body = body;
        this.messageConverter = messageConverter;
    }

    @Override
    public Void bridge(Request request, Response response) {
        try {
            this.messageConverter.write(body, response.contentType(),
                    new ServletServerHttpResponse(response.unwrap(HttpServletResponse.class)));
        } catch (IOException e) {
            throw new IllegalStateException(e); // TODO
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HttpMessageConverterHandlerBridge{");
        sb.append("body=").append(body);
        sb.append(", messageConverter=").append(messageConverter);
        sb.append('}');
        return sb.toString();
    }
}
