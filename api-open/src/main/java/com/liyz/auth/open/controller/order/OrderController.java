package com.liyz.auth.open.controller.order;

import com.liyz.auth.common.base.result.Result;
import com.liyz.auth.common.limit.annotation.Limit;
import com.liyz.auth.common.limit.annotation.Limits;
import com.liyz.auth.security.base.annotation.Anonymous;
import com.liyz.auth.service.order.remote.RemoteOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/9/1 15:37
 */
@Api(value = "订单相关接口", tags = "订单相关接口")
@ApiResponses(value = {
        @ApiResponse(code = 0, message = "成功"),
        @ApiResponse(code = 1, message = "失败"),
        @ApiResponse(code = 401, message = "暂无授权"),
        @ApiResponse(code = 404, message = "找不到资源"),
        @ApiResponse(code = 500, message = "服务器内部错误")
})
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @DubboReference(timeout = 50000)
    private RemoteOrderService remoteOrderService;

    @Anonymous
    @Limits(value = {@Limit(count = 1)})
    @ApiOperation(value = "创建订单", notes = "创建订单")
    @GetMapping("/insert")
    public Result<Boolean> insert() {
        remoteOrderService.insert("CN0001");
        return Result.success(Boolean.TRUE);
    }
}
