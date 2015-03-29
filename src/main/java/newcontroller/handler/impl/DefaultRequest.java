package newcontroller.handler.impl;

import newcontroller.handler.Request;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultRequest implements Request {
    private final HttpServletRequest request;
    private static final ConversionService DEFAULT_CONVERSION_SERVICE = new DefaultConversionService();
    private final ConversionService conversionService = DEFAULT_CONVERSION_SERVICE;

    public DefaultRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Optional<String> param(String name) {
        return Optional.ofNullable(request.getParameter(name));
    }

    @Override
    public List<String> params(String name) {
        String[] values = request.getParameterValues(name);
        return values == null ? Collections.emptyList() : Arrays.asList(values);
    }

    @Override
    public Map<String, List<String>> params() {
        return request.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, x -> Arrays.asList(x.getValue())));
    }

    @Override
    public <T> T populate(Class<T> clazz) {
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(clazz);
        T obj = BeanUtils.instantiate(clazz);
        Stream.of(descriptors)
                .filter(d -> conversionService.canConvert(String.class, d.getPropertyType()))
                .forEach(d -> {
                    String value = request.getParameter(d.getName());
                    Object converted = conversionService.convert(value, d.getPropertyType());
                    ReflectionUtils.invokeMethod(d.getWriteMethod(), obj, converted);
                });
        return obj;
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        return clazz.cast(request);
    }
}
