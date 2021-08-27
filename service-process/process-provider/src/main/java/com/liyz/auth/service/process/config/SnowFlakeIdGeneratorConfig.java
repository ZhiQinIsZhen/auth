package com.liyz.auth.service.process.config;

import com.liyz.auth.service.process.exception.RemoteProcessServiceException;
import org.activiti.engine.impl.cfg.IdGenerator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.NetworkInterface;

import static org.reflections.Reflections.log;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 14:52
 */
@Configuration
public class SnowFlakeIdGeneratorConfig implements IdGenerator, InitializingBean {

    /**
     * 起始的时间戳
     * 2021-04-01 00:00:00
     */
    private static final long START_STAMP = 1617206400000L;

    /**
     * 每一部分占用的位数
     */
    private static final long SEQUENCE_BIT = 12; //序列号占用的位数
    private static final long MACHINE_BIT = 5;   //机器标识占用的位数

    /**
     * 每一部分的最大值
     */
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_BIT);

    /**
     * 每一部分向左的位移
     */
    private static final long MACHINE_LEFT = SEQUENCE_BIT;
    private static final long TIMESTAMP_LEFT = SEQUENCE_BIT + MACHINE_BIT;

    /**
     * 机器标识
     */
    private long machineId;
    /**
     * 序列化
     */
    private long sequence = 0L;
    /**
     * 上一次时间戳
     */
    private long lastStamp = -1L;
    /**
     * 最大的重试获取时间戳次数
     */
    private static final int MAX_TRIES = 1000;

    @Override
    public synchronized String getNextId() {
        // 当前时间戳
        long currStamp;
        // 重试次数
        int tries = 0;
        do {
            currStamp = getNewStamp();
        } while (currStamp < lastStamp && ++tries <= MAX_TRIES);

        // 如果重试1000次后时间戳依然小于最后一次获取到的时间戳，直接报错
        if (currStamp < lastStamp) {
            throw new RemoteProcessServiceException("服务器发生时钟回拨，暂时不支持生成ID");
        }

        if (currStamp == lastStamp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStamp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStamp = currStamp;

        long id = (currStamp - START_STAMP) << TIMESTAMP_LEFT //时间戳部分
                | machineId << MACHINE_LEFT             //机器标识部分
                | sequence;//序列号部分
        return String.valueOf(id);
    }

    private long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastStamp) {
            mill = getNewStamp();
        }
        return mill;
    }

    private long getNewStamp() {
        return System.currentTimeMillis();
    }

    @Override
    @SuppressWarnings("all")
    public void afterPropertiesSet() {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = ((0x000000FF & (long) mac[mac.length - 2]) | (0x0000FF00 & (((long) mac[mac.length - 1]) << 8))) >> 6;
                    id = id % (MAX_MACHINE_ID + 1);
                }
            }
        } catch (Exception e) {
            log.error(" get machine id : " + e.getMessage());
        }
        log.info("当前机器ID:{}", id);
        this.machineId = id;
    }
}
