package newcontroller.handler.impl;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.List;

class HttpMessageConvertersHelper {
    static HttpMessageConverter<?> findConverter(List<HttpMessageConverter<?>> converters, Class<?> clazz, MediaType mediaType) {
        return converters.stream()
                .filter(converter -> converter.canWrite(clazz, mediaType))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot find HttpMessageConverter for clazz=" + clazz + ", mediaType=" + mediaType));
    }
}
