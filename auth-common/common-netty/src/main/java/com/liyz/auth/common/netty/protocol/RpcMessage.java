package com.liyz.auth.common.netty.protocol;

import com.liyz.auth.common.util.JsonMapperUtil;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/9 15:19
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RpcMessage {

    private int id;

    private byte messageType;

    private byte codec;

    private byte compressor;

    private Map<String, String> headMap = new HashMap<>();

    private Object body;

    @Override
    public String toString() {
        return JsonMapperUtil.toJSONString(this);
    }
}
