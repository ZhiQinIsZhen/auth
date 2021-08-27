package com.liyz.auth.service.process.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 14:15
 */
@Getter
@Setter
public class ProcessDeployBO implements Serializable {
    private static final long serialVersionUID = 7319059872451814612L;

    private byte[] bytes;

    private String fileName;
}
