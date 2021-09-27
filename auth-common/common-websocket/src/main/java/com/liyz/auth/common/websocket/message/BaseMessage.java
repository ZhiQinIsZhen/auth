package com.liyz.auth.common.websocket.message;

import com.google.common.base.Splitter;
import com.liyz.auth.common.util.JsonMapperUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/26 14:02
 */
@Getter
@Setter
public class BaseMessage {

    private String id;

    private String biz;

    private String msg;

    private String result;

    public Map<String, String> msgMap() {
        return Splitter.on("&").withKeyValueSeparator("=").split(msg);
    }

    @Override
    public String toString() {
        return JsonMapperUtil.toJSONString(this);
    }
}
