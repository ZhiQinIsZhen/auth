package com.liyz.auth.common.tcp.core;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/10 18:00
 */
public class XID {

    private static int port;

    private static String ipAddress;

    /**
     * Sets port.
     *
     * @param port the port
     */
    public static void setPort(int port) {
        XID.port = port;
    }

    /**
     * Sets ip address.
     *
     * @param ipAddress the ip address
     */
    public static void setIpAddress(String ipAddress) {
        XID.ipAddress = ipAddress;
    }

    /**
     * Generate xid string.
     *
     * @param tranId the tran id
     * @return the string
     */
    public static String generateXID(long tranId) {
        return ipAddress + ":" + port + ":" + tranId;
    }

    /**
     * Gets transaction id.
     *
     * @param xid the xid
     * @return the transaction id
     */
    public static long getTransactionId(String xid) {
        if (xid == null) {
            return -1;
        }

        int idx = xid.lastIndexOf(":");
        return Long.parseLong(xid.substring(idx + 1));
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public static int getPort() {
        return port;
    }

    /**
     * Gets ip address.
     *
     * @return the ip address
     */
    public static String getIpAddress() {
        return ipAddress;
    }
}
