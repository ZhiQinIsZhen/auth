package com.liyz.auth.common.tcp.v1.store;

import com.liyz.auth.common.tcp.v1.session.GlobalSession;
import com.liyz.auth.common.tcp.v1.session.SessionCondition;

import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/15 10:48
 */
public interface TransactionStoreManager {

    /**
     * Write session boolean.
     *
     * @param logOperation the log operation
     * @param session      the session
     * @return the boolean
     */
    boolean writeSession(LogOperation logOperation, SessionStorable session);


    /**
     * Read global session global session.
     *
     * @param xid the xid
     * @return the global session
     */
    GlobalSession readSession(String xid);

    /**
     * Read session global session.
     *
     * @param xid the xid
     * @param withBranchSessions the withBranchSessions
     * @return the global session
     */
    GlobalSession readSession(String xid, boolean withBranchSessions);

    /**
     * Read session by status list.
     *
     * @param sessionCondition the session condition
     * @return the list
     */
    List<GlobalSession> readSession(SessionCondition sessionCondition);

    /**
     * Shutdown.
     */
    void shutdown();


    /**
     * The enum Log operation.
     */
    enum LogOperation {

        /**
         * Global add log operation.
         */
        GLOBAL_ADD((byte)1),
        /**
         * Global update log operation.
         */
        GLOBAL_UPDATE((byte)2),
        /**
         * Global remove log operation.
         */
        GLOBAL_REMOVE((byte)3),
        /**
         * Branch add log operation.
         */
        BRANCH_ADD((byte)4),
        /**
         * Branch update log operation.
         */
        BRANCH_UPDATE((byte)5),
        /**
         * Branch remove log operation.
         */
        BRANCH_REMOVE((byte)6);

        private byte code;

        LogOperation(byte code) {
            this.code = code;
        }

        /**
         * Gets code.
         *
         * @return the code
         */
        public byte getCode() {
            return this.code;
        }

        /**
         * Gets log operation by code.
         *
         * @param code the code
         * @return the log operation by code
         */
        public static LogOperation getLogOperationByCode(byte code) {
            for (LogOperation temp : values()) {
                if (temp.getCode() == code) {
                    return temp;
                }
            }
            throw new IllegalArgumentException("Unknown LogOperation[" + code + "]");
        }
    }
}
