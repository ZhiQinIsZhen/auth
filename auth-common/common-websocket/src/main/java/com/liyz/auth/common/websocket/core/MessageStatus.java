package com.liyz.auth.common.websocket.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 14:18
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageStatus {

    private static final ConcurrentMap<String, MessageStatus> SERVICE_STATUS_MAP = new ConcurrentHashMap<>();
    private final AtomicLong active = new AtomicLong();
    private final LongAdder total = new LongAdder();

    /**
     * get the MessageStatus of this service
     *
     * @param channelId
     * @return RpcStatus
     */
    public static MessageStatus getStatus(String channelId) {
        return SERVICE_STATUS_MAP.computeIfAbsent(channelId, key -> new MessageStatus());
    }

    /**
     * remove the RpcStatus of this service
     *
     * @param service the service
     */
    public static void removeStatus(String service) {
        SERVICE_STATUS_MAP.remove(service);
    }

    /**
     * begin count
     *
     * @param channelId the service
     */
    public static void beginCount(String channelId) {
        getStatus(channelId).active.incrementAndGet();
    }

    /**
     * end count
     *
     * @param channelId the service
     */
    public static void endCount(String channelId) {
        MessageStatus messageStatus = getStatus(channelId);
        messageStatus.active.decrementAndGet();
        messageStatus.total.increment();
    }

    /**
     * get active.
     *
     * @return active
     */
    public long getActive() {
        return active.get();
    }

    /**
     * get total.
     *
     * @return total
     */
    public long getTotal() {
        return total.longValue();
    }
}
