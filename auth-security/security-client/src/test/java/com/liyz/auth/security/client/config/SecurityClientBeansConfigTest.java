package com.liyz.auth.security.client.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/14 14:21
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityClientBeansConfig.class})
public class SecurityClientBeansConfigTest {

    @Test
    public void test() {
        log.info("value:{}", PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("Aa123456"));
        boolean is = PasswordEncoderFactories.createDelegatingPasswordEncoder().matches("Aa123456",
                "{MD5}$2a$10$fnMEX9Ye//v8vFogfXWFU.gfr0Np6Wlsy2YfYof7zNd/qVlEID1uO");
        log.info("value : {}", is);
    }
}