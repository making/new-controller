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

import org.junit.Test;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class HttpMessageConverterHandlerBridgeTest {

    @Test
    public void testBridge() throws Exception {
        HttpServletRequest request = MockMvcRequestBuilders.get("/")
                .param("foo", "aaa")
                .param("bar", "100")
                .buildRequest(new MockServletContext());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HttpMessageConverterHandlerBridge<String> bridge = new HttpMessageConverterHandlerBridge<>(
                "Hello World!",
                new StringHttpMessageConverter());
        bridge.bridge(new DefaultRequest(request), new DefaultResponse(response));
        assertThat(response.getContentAsString(), is("Hello World!"));
    }
}