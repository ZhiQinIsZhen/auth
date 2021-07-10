package com.liyz.auth.common.base.desen;

import com.google.common.collect.Maps;
import com.liyz.auth.common.base.desen.type.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/22 17:32
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DesensitizeStrategy {

    private static Logger logger = LoggerFactory.getLogger(DesensitizeStrategy.class);

    private static volatile Map<DesensitizationType, DesensitizeService> map = Maps.newHashMap();

    /**
     * 初始化
     */
    public static void init() {
        if (CollectionUtils.isEmpty(map)) {
            synchronized (DesensitizeStrategy.class) {
                if (CollectionUtils.isEmpty(map)) {
                    map.put(DesensitizationType.DEFAULT, new DesensitizeDefault());
                    map.put(DesensitizationType.MOBILE, new DesensitizeMobile());
                    map.put(DesensitizationType.EMAIL, new DesensitizeEmail());
                    map.put(DesensitizationType.REAL_NAME, new DesensitizeName());
                    map.put(DesensitizationType.CARD_NO, new DesensitizeCardNo());
                    map.put(DesensitizationType.SELF_DEFINITION, new DesensitizeSelfDefinition());
                    map.put(DesensitizationType.IGNORE, new DesensitizeIgnore());
                }
            }
        }
    }

    /**
     * 获取
     *
     * @param type
     * @return
     */
    public static DesensitizeService getService(DesensitizationType type) {
        DesensitizeService desensitizeService = map.get(type);
        if (Objects.isNull(desensitizeService)) {
            return map.get(DesensitizationType.DEFAULT);
        }
        return desensitizeService;
    }

    static {
        logger.info("init DesensitizeStrategy start ...");
        DesensitizeStrategy.init();
        logger.info("init DesensitizeStrategy success ...");
    }
}
