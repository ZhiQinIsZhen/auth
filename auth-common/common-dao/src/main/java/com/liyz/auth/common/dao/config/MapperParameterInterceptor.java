package com.liyz.auth.common.dao.config;

import com.liyz.auth.common.dao.annotation.EncryptDecrypt;
import com.liyz.auth.common.dao.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/24 15:30
 */
@Slf4j
@Component
@Intercepts({
        @Signature(type = ParameterHandler.class, method = "setParameters", args={PreparedStatement.class})
})
public class MapperParameterInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof ParameterHandler) {
            ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
            PreparedStatement ps = (PreparedStatement) invocation.getArgs()[0];
            Field boundSqlField = parameterHandler.getClass().getDeclaredField("boundSql");
            boundSqlField.setAccessible(true);
            BoundSql boundSql = (BoundSql) boundSqlField.get(parameterHandler);
            log.debug("boundSql : {}", boundSql.getSql());
            // 反射获取 参数对像
            Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
            parameterField.setAccessible(true);
            Object parameterObject = parameterField.get(parameterHandler);
            if (Objects.nonNull(parameterObject)) {
                Set<Field> fields = check(parameterObject);
                if (!CollectionUtils.isEmpty(fields)) {
                    fields.stream().forEach(item -> {
                        try {
                            item.setAccessible(true);
                            Object value = item.get(parameterObject);
                            if (Objects.nonNull(value) && value.toString().trim().length() > 0) {
                                item.set(parameterObject, AESUtil.encryptCBC(value.toString(), "rZxl3zy!rZxl3zy!", "rZxl3zy!rZxl3zy!"));
                            }
                        } catch (Exception e) {
                            log.error("MapperParameterInterceptor.intercept invoke error", e);
                        }
                    });
                }
            }
        }
        return invocation.proceed();
    }

    /**
     * 检查哪些需要脱敏的
     *
     * @param target
     * @return
     */
    private Set<Field> check(Object target) {
        Set<Field> fieldSet = new HashSet<>();
        Class<?> cls = target.getClass();
        Field[] fields = cls.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (int i = 0, length = fields.length; i < length; i++) {
                Field field = fields[i];
                if (field.getType() != String.class || !field.isAnnotationPresent(EncryptDecrypt.class)) {
                    continue;
                }
                fieldSet.add(field);
            }
        }
        return fieldSet;
    }
}
