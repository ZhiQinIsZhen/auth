package com.liyz.auth.common.base.trace;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.threadlocal.InternalThreadLocal;

import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/7/8 17:38
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TraceContext {

    private static final InternalThreadLocal<TraceInfo> LOCAL_TRACE = new InternalThreadLocal<>();

    /**
     * 获取traceId
     *
     * @return
     */
    public static String getTraceId() {
        TraceInfo traceInfo = LOCAL_TRACE.get();
        if (Objects.nonNull(traceInfo)) {
            return traceInfo.getTraceId();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 设置traceId
     *
     * @param traceId
     * @param method
     */
    public static void setTraceId(String traceId, String method) {
        TraceInfo traceInfo = new TraceInfo();
        traceInfo.setTraceId(traceId);
        traceInfo.setMethod(method);
        traceInfo.setTimestamp(System.currentTimeMillis());
        LOCAL_TRACE.set(traceInfo);
    }

    /**
     * 移除traceId
     */
    public static void removeTraceId() {
        TraceInfo traceInfo = LOCAL_TRACE.get();
        if (Objects.nonNull(traceInfo)) {
            log.warn("traceId : {}, total request time : {} ms, method : {}",
                    traceInfo.getTraceId(),
                    System.currentTimeMillis() - traceInfo.getTimestamp(),
                    traceInfo.getMethod());
        }
    }
}
