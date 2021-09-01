package com.liyz.auth.service.order.context;

import lombok.Getter;
import lombok.Setter;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/5/11 13:51
 */
@Getter
@Setter
public class ServiceResult<T> {

    private boolean success ;

    private T result;

    private String msg;

    public ServiceResult() {
        success = true;
    }

    public ServiceResult(T result, String msg) {
        super();
        this.result = result;
        this.msg = msg;
    }

    public ServiceResult(boolean success, T result, String msg) {
        this.success = success;
        this.result = result;
        this.msg = msg;
    }
}
