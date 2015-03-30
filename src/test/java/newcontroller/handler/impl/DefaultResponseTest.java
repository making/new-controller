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
import org.junit.Test;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class DefaultResponseTest {

    @Test
    public void testBodyText() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        Response res = new DefaultResponse(response, Arrays.asList(new StringHttpMessageConverter(), new GsonHttpMessageConverter()));
        HandlerBridge handlerBridge = res.body("Hello");
        handlerBridge.bridge(new DefaultRequest(new MockHttpServletRequest()), res);
        assertThat(response.getContentAsString(), is("Hello"));
    }

    @Test
    public void testBodyJson() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        Response res = new DefaultResponse(response, Arrays.asList(new StringHttpMessageConverter(), new GsonHttpMessageConverter()));
        HandlerBridge handlerBridge = res.body(Collections.singletonMap("name", "Joy"));
        handlerBridge.bridge(new DefaultRequest(new MockHttpServletRequest()), res);
        assertThat(response.getContentAsString(), is("{\"name\":\"Joy\"}"));
    }

    @Test
    public void testView() throws Exception {

    }

    @Test
    public void testWith() throws Exception {

    }

    @Test
    public void testContentType() throws Exception {

    }

    @Test
    public void testContentType1() throws Exception {

    }

    @Test
    public void testUnwrap() throws Exception {

    }
}