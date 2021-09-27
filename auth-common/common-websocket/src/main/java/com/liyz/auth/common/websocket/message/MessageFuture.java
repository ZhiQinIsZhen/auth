package com.liyz.auth.common.websocket.message;

import com.liyz.auth.common.remote.exception.RemoteServiceException;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 14:11
 */
public class MessageFuture {

    @Getter
    @Setter
    private BaseMessage baseMessage;
    @Getter
    @Setter
    private long timeout;
    private long start = System.currentTimeMillis();
    private transient CompletableFuture<Object> origin = new CompletableFuture<>();

    /**
     * Is timeout boolean.
     *
     * @return the boolean
     */
    public boolean isTimeout() {
        return System.currentTimeMillis() - start > timeout;
    }

    /**
     * Get object.
     *
     * @param timeout
     * @param unit
     * @return
     * @throws TimeoutException
     * @throws InterruptedException
     */
    public Object get(long timeout, TimeUnit unit) throws TimeoutException,
            InterruptedException {
        Object result = null;
        try {
            result = origin.get(timeout, unit);
        } catch (ExecutionException e) {
            throw new RemoteServiceException("Should not get results in a multi-threaded environment", e);
        } catch (TimeoutException e) {
            throw new TimeoutException("cost " + (System.currentTimeMillis() - start) + " ms");
        }

        if (result instanceof RuntimeException) {
            throw (RuntimeException)result;
        } else if (result instanceof Throwable) {
            throw new RemoteServiceException("msg get error", (Throwable)result);
        }

        return result;
    }

    /**
     * Sets result message.
     *
     * @param obj the obj
     */
    public void setResultMessage(Object obj) {
        origin.complete(obj);
    }
}
