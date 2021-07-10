package com.liyz.auth.common.base.filter.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.liyz.auth.common.base.desen.DesensitizationType;
import com.liyz.auth.common.base.desen.DesensitizeStrategy;
import com.liyz.auth.common.base.trace.annotation.LogIgnore;

import java.io.IOException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/7/10 20:12
 */
public class JacksonIgnoreContextValueFilter extends JsonSerializer<Object> implements ContextualSerializer {

    private LogIgnore logIgnore;

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (logIgnore == null) {
            return;
        }
        jsonGenerator.writeString(DesensitizeStrategy.getService(DesensitizationType.IGNORE).desensitize(null, null));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            Class<?> clazz = beanProperty.getMember().getDeclaringClass();
            LogIgnore annotation = clazz.getAnnotation(LogIgnore.class);
            if (annotation != null) {
                logIgnore = annotation;
            } else {
                logIgnore = beanProperty.getAnnotation(LogIgnore.class);
            }
        }
        return this;
    }
}
