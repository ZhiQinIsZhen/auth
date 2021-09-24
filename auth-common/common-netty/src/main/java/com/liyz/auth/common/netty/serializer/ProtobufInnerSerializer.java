package com.liyz.auth.common.netty.serializer;

import com.liyz.auth.common.remote.exception.RemoteServiceException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/23 18:10
 */
public class ProtobufInnerSerializer {

    private static final ProtobufHelper PROTOBUF_HELPER = new ProtobufHelper();

    /**
     * Encode method name
     */
    private static final String METHOD_TOBYTEARRAY = "toByteArray";
    /**
     * Decode method name
     */
    private static final String METHOD_PARSEFROM = "parseFrom";

    public static byte[] serializeContent(Object request) {
        Class clazz = request.getClass();
        Method method = PROTOBUF_HELPER.toByteArrayMethodMap.computeIfAbsent(clazz, key -> {
            try {
                Method m = clazz.getMethod(METHOD_TOBYTEARRAY);
                m.setAccessible(true);
                return m;
            } catch (Exception e) {
                throw new RemoteServiceException("Cannot found method " + clazz.getName()
                        + ".toByteArray(), please check the generated code.", e);
            }
        });

        try {
            return (byte[])method.invoke(request);
        } catch (Exception e) {
            throw new RemoteServiceException("serialize occurs exception", e);
        }
    }

    public static <T> T deserializeContent(String responseClazz, byte[] content) {
        if (content == null || content.length == 0) {
            return null;
        }
        Class clazz = PROTOBUF_HELPER.getPbClass(responseClazz);

        Method method = PROTOBUF_HELPER.parseFromMethodMap.computeIfAbsent(clazz, key -> {
            try {
                Method m = clazz.getMethod(METHOD_PARSEFROM, byte[].class);
                if (!Modifier.isStatic(m.getModifiers())) {
                    throw new RemoteServiceException("Cannot found static method " + clazz.getName()
                            + ".parseFrom(byte[]), please check the generated code");
                }
                m.setAccessible(true);
                return m;
            } catch (NoSuchMethodException e) {
                throw new RemoteServiceException("Cannot found method " + clazz.getName()
                        + ".parseFrom(byte[]), please check the generated code", e);
            }
        });

        try {
            return (T)method.invoke(null, content);
        } catch (Exception e) {
            throw new RemoteServiceException("Error when invoke " + clazz.getName() + ".parseFrom(byte[]).", e);
        }
    }
}
