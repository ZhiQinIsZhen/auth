package com.liyz.auth.common.base.trace;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.threadlocal.InternalThreadLocal;

import java.util.Objects;
import java.util.UUID;

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
     * 获取traceInfo
     *
     * @return
     */
    public static TraceInfo getTraceInfo() {
        return LOCAL_TRACE.get();
    }

    /**
     * 设置traceInfo
     *
     * @param traceInfo
     */
    public static void setTraceInfo(TraceInfo traceInfo) {
        if (Objects.isNull(traceInfo.getTimestamp())) {
            traceInfo.setTimestamp(System.currentTimeMillis());
        }
        LOCAL_TRACE.set(traceInfo);
    }

    /**
     * 移除traceInfo
     */
    public static void removeTraceInfo() {
        LOCAL_TRACE.remove();
    }

    /**
     * 生成一个新的链路id
     *
     * @return
     */
    public static String createTraceId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成一个新的跨度id
     * 注：由于生成规则和链路id一样，不方便区分，应用者可以和链路id一样，重新修改其生成规则
     *
     * @return
     */
    public static String createSpanId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
