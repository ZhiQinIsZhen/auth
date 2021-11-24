package com.liyz.auth.listener;

import com.liyz.auth.common.remote.exception.CommonExceptionCodeEnum;
import com.liyz.auth.common.remote.exception.RemoteServiceException;
import com.liyz.auth.vo.UserEventVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/11/24 10:31
 */
@Slf4j
@Component
public class UserEventListener implements ApplicationListener<UserEventVO> {

    @Async
    @SneakyThrows
    @Override
    public void onApplicationEvent(UserEventVO event) {
        Thread.sleep(10000);
        log.info("userId:{}, event reserve", event.getUserId());
    }

    @Async
    @SneakyThrows
    @EventListener
    public void event(UserEventVO event) {
        Thread.sleep(5000);
        log.info("userId:{}, event reserve sleep", event.getUserId());
        if (Objects.nonNull(event)) {
            throw new RemoteServiceException(CommonExceptionCodeEnum.ParameterError);
        }
    }
}
