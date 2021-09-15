package com.liyz.auth.common.tcp.exception;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:34
 */
public class GlobalTransactionException extends TransactionException{

    /**
     * Instantiates a new Transaction exception.
     *
     * @param code the code
     */
    public GlobalTransactionException(TransactionExceptionCode code) {
        super(code);
    }

    /**
     * Instantiates a new Transaction exception.
     *
     * @param code  the code
     * @param cause the cause
     */
    public GlobalTransactionException(TransactionExceptionCode code, Throwable cause) {
        super(code, cause);
    }

    /**
     * Instantiates a new Transaction exception.
     *
     * @param message the message
     */
    public GlobalTransactionException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Transaction exception.
     *
     * @param code    the code
     * @param message the message
     */
    public GlobalTransactionException(TransactionExceptionCode code, String message) {
        super(code, message);
    }

    /**
     * Instantiates a new Transaction exception.
     *
     * @param cause the cause
     */
    public GlobalTransactionException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Transaction exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public GlobalTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Transaction exception.
     *
     * @param code    the code
     * @param message the message
     * @param cause   the cause
     */
    public GlobalTransactionException(TransactionExceptionCode code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
