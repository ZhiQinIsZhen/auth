package com.liyz.auth.open.controller.order;

import com.liyz.auth.common.base.result.Result;
import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.common.limit.annotation.Limit;
import com.liyz.auth.common.limit.annotation.Limits;
import com.liyz.auth.open.dto.order.StockDTO;
import com.liyz.auth.open.vo.order.UndoLogVO;
import com.liyz.auth.security.base.annotation.Anonymous;
import com.liyz.auth.service.order.bo.StockBO;
import com.liyz.auth.service.order.remote.RemoteStockService;
import com.liyz.auth.service.order.remote.RemoteUndoLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2022/3/2 18:05
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
@RequestMapping("/log")
public class UndoLogController {

    @DubboReference(timeout = 5000000)
    private RemoteUndoLogService remoteUndoLogService;
    @DubboReference(timeout = 5000000)
    private com.liyz.auth.service.staff.remote.RemoteUndoLogService remoteUndoLogStaffService;
    @DubboReference(timeout = 5000000)
    private RemoteStockService remoteStockService;

    @Anonymous
    @Limits(value = {@Limit(count = 1)})
    @ApiOperation(value = "order undoLog 查询", notes = "order undoLog 查询")
    @GetMapping("/order")
    public Result<List<UndoLogVO>> list() {
        return Result.success(CommonCloneUtil.ListClone(remoteUndoLogService.list(), UndoLogVO.class));
    }

    @Anonymous
    @Limits(value = {@Limit(count = 1)})
    @ApiOperation(value = "staff undoLog 查询", notes = "staff undoLog 查询")
    @GetMapping("/staff")
    public Result<List<UndoLogVO>> listOrder() {
        return Result.success(CommonCloneUtil.ListClone(remoteUndoLogStaffService.list(), UndoLogVO.class));
    }

    @Anonymous
    @Limits(value = {@Limit(count = 1)})
    @ApiOperation(value = "减库存", notes = "减库存")
    @GetMapping("/sub")
    public Result<Boolean> sub(StockDTO stockDTO) {
        remoteStockService.subAmount(CommonCloneUtil.objectClone(stockDTO, StockBO.class));
        return Result.success(Boolean.TRUE);
    }
}
