package com.liyz.auth.common.tcp.core;

import com.liyz.auth.common.tcp.exception.TransactionException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:29
 */
public interface Lockable {

    /**
     * Lock boolean.
     *
     * @return the boolean
     * @throws TransactionException the transaction exception
     */
    boolean lock() throws TransactionException;

    /**
     * Unlock boolean.
     *
     * @return the boolean
     * @throws TransactionException the transaction exception
     */
    boolean unlock() throws TransactionException;
}
