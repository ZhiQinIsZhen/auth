package com.liyz.auth.common.remote.page;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/4/12 15:12
 */
@Getter
@Setter
public class PageBaseBO implements Serializable {
    private static final long serialVersionUID = -8535681522296889339L;

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}
