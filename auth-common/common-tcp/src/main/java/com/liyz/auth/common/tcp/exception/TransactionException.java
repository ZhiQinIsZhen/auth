package com.liyz.auth.common.tcp.exception;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:04
 */
public class TransactionException extends Exception{

    /**
     * The Code.
     */
    protected TransactionExceptionCode code = TransactionExceptionCode.Unknown;

    /**
     * Gets code.
     *
     * @return the code
     */
    public TransactionExceptionCode getCode() {
        return code;
    }

    /**
     * Instantiates a new Transaction exception.
     *
     * @param code the code
     */
    public TransactionException(TransactionExceptionCode code) {
        this.code = code;
    }

    /**
     * Instantiates a new Transaction exception.
     *
     * @param code  the code
     * @param cause the cause
     */
    public TransactionException(TransactionExceptionCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    /**
     * Instantiates a new Transaction exception.
     *
     * @param message the message
     */
    public TransactionException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Transaction exception.
     *
     * @param code    the code
     * @param message the message
     */
    public TransactionException(TransactionExceptionCode code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * Instantiates a new Transaction exception.
     *
     * @param cause the cause
     */
    public TransactionException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new Transaction exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Transaction exception.
     *
     * @param code    the code
     * @param message the message
     * @param cause   the cause
     */
    public TransactionException(TransactionExceptionCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
