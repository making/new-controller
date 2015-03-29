/*
*Copyright (C) 2015 Toshiaki Maki <makingx@gmail.com>
*
*Licensed under the Apache License, Version 2.0 (the "License");
*you may not use this file except in compliance with the License.
*You may obtain a copy of the License at
*
*        http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing, software
*distributed under the License is distributed on an "AS IS" BASIS,
*WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*See the License for the specific language governing permissions and
*limitations under the License.
*/
package newcontroller;

import newcontroller.support.CapturedHttpServletRequest;
import newcontroller.support.CapturedHttpServletRequestAdopter;
import me.geso.routes.RoutingResult;
import me.geso.routes.WebRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public class RouterHandlerMapping<T> extends AbstractHandlerMapping {
    @Autowired
    Optional<List<RouterDefinition<T>>> routerDefinitions;
    private final static Logger log = LoggerFactory.getLogger(RouterHandlerMapping.class);
    private final WebRouter<T> router = new WebRouter<>();
    private final HandlerApplier<T> handlerApplier;

    public RouterHandlerMapping(HandlerApplier<T> handlerApplier) {
        this.handlerApplier = handlerApplier;
    }

    @FunctionalInterface
    public static interface HandlerApplier<T> {
        ModelAndView apply(T handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
    }

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        if (request.getRequestURI().equalsIgnoreCase("favicon.ico")) {
            return null;
        }
        String method = request.getMethod();
        String path = request.getRequestURI();
        return this.router.match(method, path);
    }

    @PostConstruct
    public void init() {
        this.routerDefinitions.ifPresent(defs -> {
            for (RouterDefinition<T> def : defs) {
                def.define(this.router);
            }
        });
        this.router.getPatterns().forEach(x -> log.info("Router(path={}, method={})\t->\t{}",
                x.getPath(), x.getMethods(), x.getDestination()));
    }

    public HandlerAdapter handlerAdapter() {
        return new HandlerAdapter() {
            @Override
            public boolean supports(Object handler) {
                return RoutingResult.class.isAssignableFrom(handler.getClass());
            }

            @Override
            @SuppressWarnings("unchecked")
            public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                RoutingResult<T> routingResult = (RoutingResult<T>) handler;
                CapturedHttpServletRequest req = new CapturedHttpServletRequestAdopter(routingResult.getCaptured(), request);
                return RouterHandlerMapping.this.handlerApplier.apply(routingResult.getDestination(), req, response);
            }

            @Override
            public long getLastModified(HttpServletRequest request, Object handler) {
                return -1;
            }
        };
    }
}
