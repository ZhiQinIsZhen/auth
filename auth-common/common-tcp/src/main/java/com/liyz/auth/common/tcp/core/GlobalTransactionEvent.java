package com.liyz.auth.common.tcp.core;

import com.liyz.auth.common.tcp.v1.core.model.GlobalStatus;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/14 10:59
 */
public class GlobalTransactionEvent implements Event{

    public static final String ROLE_TC = "tc";

    public static final String ROLE_TM = "tm";

    public static final String ROLE_RM = "rm";

    /**
     * Transaction Id
     */
    private long id;

    /**
     * Source Role
     */
    private final String role;

    /**
     * Transaction Name
     */
    private final String name;

    /**
     * Transaction Begin Time
     */
    private final Long beginTime;

    /**
     * Transaction End Time (If Transaction do not committed or rollbacked, null)
     */
    private final Long endTime;

    /**
     * Transaction Status
     */
    private final GlobalStatus status;

    public long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public GlobalStatus getStatus() {
        return status;
    }

    public GlobalTransactionEvent(long id, String role, String name, Long beginTime, Long endTime,
                                  GlobalStatus status) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.status = status;
    }
}
