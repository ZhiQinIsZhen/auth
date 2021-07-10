package com.liyz.auth.common.base.trace;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/7/8 17:40
 */
@Getter
@Setter
public class TraceInfo implements Serializable {
    private static final long serialVersionUID = 6011195225688642301L;

    /**
     * 标记一次请求的跟踪，相关的Spans都有相同的traceId
     */
    private String traceId;

    /**
     * 跨度id
     */
    private String spanId;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 父跨度id
     */
    private String parentSpanId;

    /**
     * 开始时间戳
     */
    private Long timestamp;

    /**
     * 持续时间
     */
    private Long duration;

    /**
     * 参数
     */
    private String params;

    /**
     * 结果
     */
    private String result;

    /**
     * 是否正常结束
     */
    private Boolean isNormal = Boolean.TRUE;
}
