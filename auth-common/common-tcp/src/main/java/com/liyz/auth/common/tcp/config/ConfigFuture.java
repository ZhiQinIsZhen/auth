package com.liyz.auth.common.tcp.config;

import com.liyz.auth.common.remote.exception.RemoteServiceException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/10 16:56
 */
@Slf4j
public class ConfigFuture {

    private static final long DEFAULT_CONFIG_TIMEOUT = 5 * 1000;
    private long timeoutMills;
    private long start = System.currentTimeMillis();
    private String dataId;
    private String content;
    private ConfigOperation operation;
    private transient CompletableFuture<Object> origin = new CompletableFuture<>();

    /**
     * Instantiates a new Config future.
     *
     * @param dataId    the data id
     * @param content   the content
     * @param operation the operation
     */
    public ConfigFuture(String dataId, String content, ConfigOperation operation) {
        this(dataId, content, operation, DEFAULT_CONFIG_TIMEOUT);
    }

    /**
     * Instantiates a new Config future.
     *
     * @param dataId       the data id
     * @param content      the content
     * @param operation    the operation
     * @param timeoutMills the timeout mills
     */
    public ConfigFuture(String dataId, String content, ConfigOperation operation, long timeoutMills) {
        this.dataId = dataId;
        this.content = content;
        this.operation = operation;
        this.timeoutMills = timeoutMills;
    }

    /**
     * Gets timeout mills.
     *
     * @return the timeout mills
     */
    public boolean isTimeout() {
        return System.currentTimeMillis() - start > timeoutMills;
    }

    /**
     * Get object.
     *
     * @return the object
     */
    public Object get() {
        return get(this.timeoutMills, TimeUnit.MILLISECONDS);
    }

    /**
     * Get object.
     *
     * @param timeout the timeout
     * @param unit    the unit
     * @return the object
     */
    public Object get(long timeout, TimeUnit unit) {
        this.timeoutMills = unit.toMillis(timeout);
        Object result;
        try {
            result = origin.get(timeout, unit);
        } catch (ExecutionException e) {
            throw new RemoteServiceException("Should not get results in a multi-threaded environment", e);
        } catch (TimeoutException e) {
            log.error("config operation timeout,cost:{} ms,op:{},dataId:{}", System.currentTimeMillis() - start, operation.name(), dataId);
            return getFailResult();
        } catch (InterruptedException exx) {
            log.error("config operate interrupted,error:{}", exx.getMessage(), exx);
            return getFailResult();
        }
        if (operation == ConfigOperation.GET) {
            return result == null ? content : result;
        } else {
            return result == null ? Boolean.FALSE : result;
        }
    }

    private Object getFailResult() {
        if (operation == ConfigOperation.GET) {
            return content;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Sets result.
     *
     * @param result the result
     */
    public void setResult(Object result) {
        origin.complete(result);
    }

    /**
     * Gets data id.
     *
     * @return the data id
     */
    public String getDataId() {
        return dataId;
    }

    /**
     * Sets data id.
     *
     * @param dataId the data id
     */
    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets operation.
     *
     * @return the operation
     */
    public ConfigOperation getOperation() {
        return operation;
    }

    /**
     * Sets operation.
     *
     * @param operation the operation
     */
    public void setOperation(ConfigOperation operation) {
        this.operation = operation;
    }

    /**
     * The enum Config operation.
     */
    public enum ConfigOperation {
        /**
         * Get config operation.
         */
        GET,
        /**
         * Put config operation.
         */
        PUT,
        /**
         * Putifabsent config operation.
         */
        PUTIFABSENT,
        /**
         * Remove config operation.
         */
        REMOVE
    }
}
