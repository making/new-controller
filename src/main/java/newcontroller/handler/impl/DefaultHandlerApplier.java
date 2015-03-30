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

import newcontroller.RouterHandlerMapping;
import newcontroller.handler.Handler;
import newcontroller.handler.HandlerBridge;
import newcontroller.handler.Request;
import newcontroller.handler.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class DefaultHandlerApplier implements RouterHandlerMapping.HandlerApplier<Handler> {
    private final List<HttpMessageConverter<?>> converters;

    @Autowired
    public DefaultHandlerApplier(List<HttpMessageConverter<?>> converters) {
        this.converters = converters;
    }

    @Override
    public ModelAndView apply(Handler handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Request req = new DefaultRequest(request, this.converters);
        Response res = new DefaultResponse(response, this.converters);
        HandlerBridge<?> handlerBridge = handler.handleRequest(req, res);
        Object bridged = handlerBridge.bridge(req, res);
        if (bridged instanceof ModelAndView) {
            return ModelAndView.class.cast(bridged);
        }
        return null;
    }
}
