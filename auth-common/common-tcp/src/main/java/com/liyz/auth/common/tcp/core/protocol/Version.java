package com.liyz.auth.common.tcp.core.protocol;

import com.liyz.auth.common.tcp.util.NetUtil;
import com.liyz.auth.common.remote.exception.RemoteServiceException;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/10 17:46
 */
public class Version {

    /**
     * The constant CURRENT.
     */
    private static final String CURRENT = "1.4.1";
    private static final String VERSION_0_7_1 = "0.7.1";
    private static final int MAX_VERSION_DOT = 3;

    /**
     * The constant VERSION_MAP.
     */
    public static final Map<String, String> VERSION_MAP = new ConcurrentHashMap<>();

    private Version() {

    }

    public static String getCurrent() {
        return CURRENT;
    }

    /**
     * Put channel version.
     *
     * @param c the c
     * @param v the v
     */
    public static void putChannelVersion(Channel c, String v) {
        VERSION_MAP.put(NetUtil.toStringAddress(c.remoteAddress()), v);
    }

    /**
     * Gets channel version.
     *
     * @param c the c
     * @return the channel version
     */
    public static String getChannelVersion(Channel c) {
        return VERSION_MAP.get(NetUtil.toStringAddress(c.remoteAddress()));
    }

    /**
     * Check version string.
     *
     * @param version the version
     * @return the string
     * @throws com.liyz.auth.common.remote.exception.RemoteServiceException
     */
    public static void checkVersion(String version) throws RemoteServiceException {
        long current = convertVersion(CURRENT);
        long clientVersion = convertVersion(version);
        long divideVersion = convertVersion(VERSION_0_7_1);
        if ((current > divideVersion && clientVersion < divideVersion) || (current < divideVersion && clientVersion > divideVersion)) {
            throw new RemoteServiceException("incompatible client version:" + version);
        }
    }

    private static long convertVersion(String version) throws RemoteServiceException {
        String[] parts = StringUtils.split(version, '.');
        long result = 0L;
        int i = 1;
        int size = parts.length;
        if (size > MAX_VERSION_DOT + 1) {
            throw new RemoteServiceException("incompatible version format:" + version);
        }
        size = MAX_VERSION_DOT + 1;
        for (String part : parts) {
            if (StringUtils.isNumeric(part)) {
                result += calculatePartValue(part, size, i);
            } else {
                String[] subParts = StringUtils.split(part, '-');
                if (StringUtils.isNumeric(subParts[0])) {
                    result += calculatePartValue(subParts[0], size, i);
                }
            }

            i++;
        }
        return result;
    }

    private static long calculatePartValue(String partNumeric, int size, int index) {
        return Long.parseLong(partNumeric) * Double.valueOf(Math.pow(100, size - index)).longValue();
    }
}
