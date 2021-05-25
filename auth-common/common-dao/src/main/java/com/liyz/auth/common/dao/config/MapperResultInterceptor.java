package com.liyz.auth.common.dao.config;

import com.liyz.auth.common.dao.annotation.Desensitization;
import com.liyz.auth.common.dao.annotation.EncryptDecrypt;
import com.liyz.auth.common.dao.constant.MapperType;
import com.liyz.auth.common.dao.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.*;

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
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args={Statement.class})
})
public class MapperResultInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (Objects.isNull(result)){
            return null;
        }
        if (result instanceof ArrayList) {
            ArrayList resultList = (ArrayList) result;
            if (!CollectionUtils.isEmpty(resultList)) {
                Map<String, Set<Field>> map = check(resultList.get(0));
                //脱敏处理
                desensitization(map ,resultList);
                //解密处理
                decrypt(map, resultList);
            }
        }
        return result;
    }

    /**
     * 检查哪些需要脱敏的
     *
     * @param target
     * @return
     */
    private Map<String, Set<Field>> check(Object target) {
        Map<String, Set<Field>> map = new HashMap<>();
        Set<Field> fieldSet = new HashSet<>();
        map.put(MapperType.DESENSITIZATION, fieldSet);
        Set<Field> fieldEncrypt = new HashSet<>();
        map.put(MapperType.ENCRYPT_DECRYPT, fieldEncrypt);
        Class<?> cls = target.getClass();
        Field[] fields = cls.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (int i = 0, length = fields.length; i < length; i++) {
                Field field = fields[i];
                if (field.getType() == String.class) {
                    if (field.isAnnotationPresent(Desensitization.class)) {
                        fieldSet.add(field);
                    }
                    if (field.isAnnotationPresent(EncryptDecrypt.class)) {
                        fieldEncrypt.add(field);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 脱敏处理
     *
     * @param map
     * @param resultList
     */
    private void desensitization(Map<String, Set<Field>> map, ArrayList resultList) {
        Set<Field> fields;
        if (CollectionUtils.isEmpty(map) || CollectionUtils.isEmpty(fields = map.get(MapperType.DESENSITIZATION))) {
            return;
        }
        for (Object obj : resultList) {
            fields.stream().forEach(field -> {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (Objects.nonNull(value) && value.toString().trim().length() > 0) {
                        field.set(obj, value.toString().substring(0, 1).concat("***"));
                    }
                } catch (Exception e) {
                    log.error("MapperResultInterceptor.intercept.desensitization invoke error", e);
                }
            });
        }
    }

    /**
     * 解密
     *
     * @param map
     * @param resultList
     */
    private void decrypt(Map<String, Set<Field>> map, ArrayList resultList) {
        Set<Field> fields;
        if (CollectionUtils.isEmpty(map) || CollectionUtils.isEmpty(fields = map.get(MapperType.ENCRYPT_DECRYPT))) {
            return;
        }
        for (Object obj : resultList) {
            fields.stream().forEach(field -> {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    if (Objects.nonNull(value) && value.toString().trim().length() > 0) {
                        field.set(obj, AESUtil.decryptCBC(value.toString(), "rZxl3zy!rZxl3zy!", "rZxl3zy!rZxl3zy!"));
                    }
                } catch (Exception e) {
                    log.error("MapperResultInterceptor.intercept.decrypt invoke error", e);
                }
            });
        }
    }
}
