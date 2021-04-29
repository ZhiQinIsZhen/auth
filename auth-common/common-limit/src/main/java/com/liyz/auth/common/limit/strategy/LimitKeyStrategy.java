package com.liyz.auth.common.limit.strategy;

import com.google.common.collect.Maps;
import com.liyz.auth.common.limit.enums.LimitType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/28 16:49
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LimitKeyStrategy {

    private static volatile Map<LimitType, LimitKeyService> map = Maps.newHashMap();

    /**
     * 获取key的策略服务
     *
     * @param limitType
     * @return
     */
    public static LimitKeyService getService(LimitType limitType) {
        return map.get(limitType);
    }

    static {
        map.put(LimitType.IP, new IPLimitKeyServiceImpl());
        map.put(LimitType.TOTAL, new TotalCountLimitKeyServiceImpl());
        map.put(LimitType.MAPPING, new MappingLimitKeyServiceImpl());
        map.put(LimitType.CUSTOMIZE, new CustomizeLimitKeyServiceImpl());
    }
}
