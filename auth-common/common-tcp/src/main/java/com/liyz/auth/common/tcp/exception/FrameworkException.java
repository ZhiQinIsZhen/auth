package com.liyz.auth.common.tcp.exception;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:13
 */
@Slf4j
public class FrameworkException extends RuntimeException{

    private static final long serialVersionUID = 5531074229174745826L;

    private final FrameworkErrorCode errcode;

    /**
     * Instantiates a new Framework exception.
     */
    public FrameworkException() {
        this(FrameworkErrorCode.UnknownAppError);
    }

    /**
     * Instantiates a new Framework exception.
     *
     * @param err the err
     */
    public FrameworkException(FrameworkErrorCode err) {
        this(err.getErrMessage(), err);
    }

    /**
     * Instantiates a new Framework exception.
     *
     * @param msg the msg
     */
    public FrameworkException(String msg) {
        this(msg, FrameworkErrorCode.UnknownAppError);
    }

    /**
     * Instantiates a new Framework exception.
     *
     * @param msg     the msg
     * @param errCode the err code
     */
    public FrameworkException(String msg, FrameworkErrorCode errCode) {
        this(null, msg, errCode);
    }

    /**
     * Instantiates a new Framework exception.
     *
     * @param cause   the cause
     * @param msg     the msg
     * @param errCode the err code
     */
    public FrameworkException(Throwable cause, String msg, FrameworkErrorCode errCode) {
        super(msg, cause);
        this.errcode = errCode;
    }

    /**
     * Instantiates a new Framework exception.
     *
     * @param th the th
     */
    public FrameworkException(Throwable th) {
        this(th, th.getMessage());
    }

    /**
     * Instantiates a new Framework exception.
     *
     * @param th  the th
     * @param msg the msg
     */
    public FrameworkException(Throwable th, String msg) {
        this(th, msg, FrameworkErrorCode.UnknownAppError);
    }

    /**
     * Gets errcode.
     *
     * @return the errcode
     */
    public FrameworkErrorCode getErrcode() {
        return errcode;
    }

    /**
     * Nested exception framework exception.
     *
     * @param e the e
     * @return the framework exception
     */
    public static FrameworkException nestedException(Throwable e) {
        return nestedException("", e);
    }

    /**
     * Nested exception framework exception.
     *
     * @param msg the msg
     * @param e   the e
     * @return the framework exception
     */
    public static FrameworkException nestedException(String msg, Throwable e) {
        log.error(msg, e.getMessage(), e);
        if (e instanceof FrameworkException) {
            return (FrameworkException)e;
        }

        return new FrameworkException(e, msg);
    }

    /**
     * Nested sql exception sql exception.
     *
     * @param e the e
     * @return the sql exception
     */
    public static SQLException nestedSQLException(Throwable e) {
        return nestedSQLException("", e);
    }

    /**
     * Nested sql exception sql exception.
     *
     * @param msg the msg
     * @param e   the e
     * @return the sql exception
     */
    public static SQLException nestedSQLException(String msg, Throwable e) {
        log.error(msg, e.getMessage(), e);
        if (e instanceof SQLException) {
            return (SQLException)e;
        }

        return new SQLException(e);
    }
}
