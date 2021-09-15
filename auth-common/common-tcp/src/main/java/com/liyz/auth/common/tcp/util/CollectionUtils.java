package com.liyz.auth.common.tcp.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/9 15:26
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionUtils {

    /**
     * Is empty boolean.
     *
     * @param col
     * @return
     */
    public static boolean isEmpty(Collection<?> col) {
        return !isNotEmpty(col);
    }

    /**
     * Is not empty boolean.
     *
     * @param col
     * @return
     */
    public static boolean isNotEmpty(Collection<?> col) {
        return col != null && !col.isEmpty();
    }

    /**
     * Is empty boolean.
     *
     * @param array
     * @return
     */
    public static boolean isEmpty(Object[] array) {
        return !isNotEmpty(array);
    }

    /**
     * Is not empty boolean.
     *
     * @param array
     * @return
     */
    public static boolean isNotEmpty(Object[] array) {
        return array != null && array.length > 0;
    }

    /**
     * Is empty boolean.
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return !isNotEmpty(map);
    }

    /**
     * Is not empty boolean.
     *
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    /**
     * To string.
     *
     * @param col
     * @return
     */
    public static String toString(Collection<?> col) {
        if (isEmpty(col)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Object obj : col) {
            sb.append(toString(obj));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    /**
     * To string.
     *
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj.getClass().isPrimitive()) {
            return String.valueOf(obj);
        }
        if (obj instanceof String) {
            return (String)obj;
        }
        if (obj instanceof Number || obj instanceof Character || obj instanceof Boolean) {
            return String.valueOf(obj);
        }
        if (obj instanceof Date) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(obj);
        }
        if (obj instanceof Collection) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            if (!((Collection)obj).isEmpty()) {
                for (Object o : (Collection)obj) {
                    sb.append(toString(o)).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("]");
            return sb.toString();
        }
        if (obj instanceof Map) {
            Map<Object, Object> map = (Map)obj;
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            if (!map.isEmpty()) {
                map.forEach((key, value) -> {
                    sb.append(toString(key)).append("->")
                            .append(toString(value)).append(",");
                });
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("}");
            return sb.toString();
        }
        StringBuilder sb = new StringBuilder();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            sb.append(field.getName());
            sb.append("=");
            try {
                Object f = field.get(obj);
                if (f.getClass() == obj.getClass()) {
                    sb.append(f.toString());
                } else {
                    sb.append(toString(f));
                }
            } catch (Exception e) {
            }
            sb.append(";");
        }
        return sb.toString();
    }

    /**
     * To string map
     *
     * @param param
     * @return
     */
    public static Map<String, String> toStringMap(Map<String, Object> param) {
        Map<String, String> covertMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(param)) {
            param.forEach((key, value) -> {
                if (value != null) {
                    covertMap.put(key, toString(value));
                }
            });
        }
        return covertMap;
    }

    private static final String KV_SPLIT = "=";

    private static final String PAIR_SPLIT = "&";

    /**
     * Encode map to string
     *
     * @param map
     * @return
     */
    public static String encodeMap(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        if (map.isEmpty()) {
            return StringUtils.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(KV_SPLIT).append(entry.getValue()).append(PAIR_SPLIT);
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * Decode string to map
     *
     * @param data
     * @return
     */
    public static Map<String, String> decodeMap(String data) {
        if (data == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(data)) {
            return map;
        }
        String[] kvPairs = data.split(PAIR_SPLIT);
        if (kvPairs.length == 0) {
            return map;
        }
        for (String kvPair : kvPairs) {
            if ((kvPair == null) || (kvPair.isEmpty())) {
                continue;
            }
            String[] kvs = kvPair.split(KV_SPLIT);
            if (kvs.length != 2) {
                continue;
            }
            map.put(kvs[0], kvs[1]);
        }
        return map;
    }

    /**
     * Compute if absent.
     * Use this method if you are frequently using the same key,
     * because the get method has no lock.
     *
     * @param map
     * @param key
     * @param mappingFunction
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> V computeIfAbsent(Map<K, V> map, K key, Function<? super K, ? extends V> mappingFunction) {
        V value = map.get(key);
        if (value != null) {
            return value;
        }
        return map.computeIfAbsent(key, mappingFunction);
    }

    public static <T> T getLast(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }

        int size;
        while (true) {
            size = list.size();
            if (size == 0) {
                return null;
            }

            try {
                return list.get(size - 1);
            } catch (IndexOutOfBoundsException ex) {
                // catch the exception and continue to retry
            }
        }
    }
}
