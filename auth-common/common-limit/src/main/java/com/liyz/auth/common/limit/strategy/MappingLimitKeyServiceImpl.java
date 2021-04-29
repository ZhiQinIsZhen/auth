package com.liyz.auth.common.limit.strategy;

import com.liyz.auth.common.base.util.HttpRequestUtil;
import com.liyz.auth.common.limit.annotation.Limit;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/29 14:07
 */
@Slf4j
public class MappingLimitKeyServiceImpl implements LimitKeyService{

    @Override
    public String getKey(Limit limit) {
        HttpServletRequest httpServletRequest = HttpRequestUtil.getRequest();
        if (Objects.nonNull(httpServletRequest)) {
            return httpServletRequest.getServletPath();
        } else {
            return "";
        }
    }

    @Override
    public double getTotalCount(Limit limit) {
        return limit.count();
    }
}
