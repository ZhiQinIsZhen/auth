package com.liyz.auth.open.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/3/2 14:32
 */
@Getter
@Setter
public class OrderDTO implements Serializable {
    private static final long serialVersionUID = -8539840842036854779L;

    @ApiModelProperty(value = "订单code", example = "159887859", required = true)
    @NotBlank(message = "订单code不能为空", groups = {Order.class})
    private String orderCode;

    public interface Order {}
}
