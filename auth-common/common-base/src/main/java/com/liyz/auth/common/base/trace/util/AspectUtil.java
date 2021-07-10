package com.liyz.auth.common.base.trace.util;

import com.google.common.collect.Sets;
import com.liyz.auth.common.base.trace.annotation.LogIgnore;
import com.liyz.auth.common.base.util.JsonMapperUtil;
import lombok.experimental.UtilityClass;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.Set;

/**
 * 注释:切面工具类
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/7/8 20:52
 */
@UtilityClass
public class AspectUtil {

    /**
     * 获取切面参数字符串
     *
     * @param joinPoint
     * @return
     */
    public static String getAspectMethodParams(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (joinPoint.getSignature() instanceof MethodSignature) {
            Set<Integer> ignoreSet = Sets.newTreeSet();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            //得到方法的参数名
            String[] parameterNames = methodSignature.getParameterNames();
            //得到当前方法的参数值
            Object[] args = joinPoint.getArgs();
            //得到方法中的每个参数的注解列表
            Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
            for (int i = 0, j = parameterAnnotations.length; i < j; i++) {
                Annotation[] annotations = parameterAnnotations[i];
                for (Annotation annotation : annotations) {
                    if (annotation instanceof LogIgnore) {
                        ignoreSet.add(i);
                    }
                }
            }
            int argsSize = Objects.isNull(args) ? 0 : args.length;
            if (parameterNames != null && parameterNames.length > 0) {
                for (int i = 0, j = parameterNames.length; i < j; i++) {
                    if (ignoreSet.contains(i)) {
                        continue;
                    }
                    sb.append(parameterNames[i]).append("=");
                    sb.append(i < argsSize ? Objects.nonNull(args[i]) ? JsonMapperUtil.toJSONString(args[i]) : "null" : "null");
                    if (i + 1 < j) {
                        sb.append("; ");
                    }
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 获取dubbo参数字符串
     *
     * @param invoker
     * @param invocation
     * @return
     */
    public static String getDubboMethodParams(Invoker<?> invoker, Invocation invocation) {
        Annotation[][] parameterAnnotations = null;
        Parameter[] parameters = null;
        try {
            Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            parameters = method.getParameters();
            parameterAnnotations = method.getParameterAnnotations();
        } catch (NoSuchMethodException e) {}
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Object[] args = invocation.getArguments();
        Class<?>[] classes = invocation.getParameterTypes();
        int i, j;
        if (classes != null && (j = classes.length) > 0) {
            Set<Integer> ignoreSet = Sets.newTreeSet();
            if (parameterAnnotations != null && parameterAnnotations.length > 0) {
                for (int k = 0, m = parameterAnnotations.length; k < m; k++) {
                    Annotation[] annotations = parameterAnnotations[k];
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof LogIgnore) {
                            ignoreSet.add(k);
                        }
                    }
                }
            }
            int argsSize = Objects.isNull(args) ? 0 : args.length;
            int paramLength = parameters == null ? 0 : parameters.length;
            for (i = 0; i < j; i++) {
                if (ignoreSet.contains(i)) {
                    continue;
                }
                sb.append(i < paramLength ? parameters[i].getName() : sb.append(classes[i].getSimpleName())).append("=");
                sb.append(i < argsSize ? Objects.nonNull(args[i]) ? JsonMapperUtil.toJSONString(args[i]) : "null" : "null");
                if (i + 1 < j) {
                    sb.append("; ");
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
