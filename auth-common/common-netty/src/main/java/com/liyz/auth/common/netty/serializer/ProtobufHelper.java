package com.liyz.auth.common.netty.serializer;

import com.google.protobuf.MessageLite;
import com.liyz.auth.common.remote.exception.RemoteServiceException;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 18:11
 */
public class ProtobufHelper {

    /**
     * Cache of parseFrom method
     */
    ConcurrentMap<Class, Method> parseFromMethodMap = new ConcurrentHashMap<>();

    /**
     * Cache of toByteArray method
     */
    ConcurrentMap<Class, Method> toByteArrayMethodMap = new ConcurrentHashMap<>();

    /**
     *  {className:class}
     */
    private ConcurrentMap<String, Class> requestClassCache = new ConcurrentHashMap<>();

    /**
     *
     * @param clazzName
     * @return
     */
    public Class getPbClass(String clazzName) {
        return requestClassCache.computeIfAbsent(clazzName, key -> {
            // get the parameter and result
            Class clazz;
            try {
                clazz = Class.forName(clazzName);
            } catch (ClassNotFoundException e) {
                throw new RemoteServiceException("get class occurs exception", e);
            }
            if (clazz == void.class || !isProtoBufMessageClass(clazz)) {
                throw new RemoteServiceException("class based protobuf: " + clazz.getName()
                        + ", only support return protobuf message!");
            }
            return clazz;
        });
    }

    /**
     * Is this class is assignable from MessageLite
     *
     * @param clazz unknown class
     * @return is assignable from MessageLite
     */
    boolean isProtoBufMessageClass(Class clazz) {
        return clazz != null && MessageLite.class.isAssignableFrom(clazz);
    }
}
