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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

public class ModelAndViewHandlerBridge implements HandlerBridge<ModelAndView> {
    private final ModelAndView modelAndView;

    public ModelAndViewHandlerBridge(View view) {
        this.modelAndView = new ModelAndView(view);
    }

    public ModelAndViewHandlerBridge(String viewName) {
        this.modelAndView = new ModelAndView(viewName);
    }

    @Override
    public ModelAndView bridge(Request request, Response response) {
        this.modelAndView.addAllObjects(request.model());
        return this.modelAndView;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ModelAndViewHandlerBridge{");
        sb.append("modelAndView=").append(modelAndView);
        sb.append('}');
        return sb.toString();
    }
}
