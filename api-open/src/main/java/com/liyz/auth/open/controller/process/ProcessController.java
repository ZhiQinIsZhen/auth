package com.liyz.auth.open.controller.process;

import com.liyz.auth.common.base.result.Result;
import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.common.limit.annotation.Limit;
import com.liyz.auth.common.limit.annotation.Limits;
import com.liyz.auth.open.dto.process.ProcessFormDTO;
import com.liyz.auth.open.vo.process.ProcessFormVO;
import com.liyz.auth.open.vo.process.ProcessInfoVO;
import com.liyz.auth.open.vo.process.TaskInfoVO;
import com.liyz.auth.security.base.annotation.NonAuthority;
import com.liyz.auth.security.client.AuthContext;
import com.liyz.auth.service.process.bo.ProcessFormBO;
import com.liyz.auth.service.process.bo.ProcessInfoBO;
import com.liyz.auth.service.process.remote.RemoteProcessService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 17:16
 */
@Api(value = "流程相关接口", tags = "流程相关接口")
@ApiResponses(value = {
        @ApiResponse(code = 0, message = "成功"),
        @ApiResponse(code = 1, message = "失败"),
        @ApiResponse(code = 401, message = "暂无授权"),
        @ApiResponse(code = 404, message = "找不到资源"),
        @ApiResponse(code = 500, message = "服务器内部错误")
})
@Slf4j
@RestController
@RequestMapping("/process")
public class ProcessController {

    @DubboReference
    private RemoteProcessService remoteProcessService;

    @NonAuthority
    @Limits(value = {@Limit(count = 1)})
    @ApiOperation(value = "发起流程", notes = "发起流程")
    @PostMapping("/start")
    @ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "String",
            paramType = "header")
    public Result<ProcessFormVO> start(@RequestBody @Validated(ProcessFormDTO.Start.class) ProcessFormDTO processFormDTO) {
        processFormDTO.setApplicant(AuthContext.getAuthUser().getUserId().toString());
        ProcessFormBO processFormBO = remoteProcessService.startProcess(CommonCloneUtil.objectClone(processFormDTO, ProcessFormBO.class));
        return Result.success(CommonCloneUtil.objectClone(processFormBO, ProcessFormVO.class));
    }

    @NonAuthority
    @Limits(value = {@Limit(count = 1)})
    @ApiOperation(value = "通过流程实例ID查询流程详情", notes = "通过流程实例ID查询流程详情")
    @GetMapping("/info")
    @ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "String",
            paramType = "header")
    public Result<ProcessInfoVO> info(@RequestParam("processInstanceId") @ApiParam(value = "流程ID") String processInstanceId) {
        ProcessInfoBO processInfoBO = remoteProcessService.processInfo(processInstanceId);
        ProcessInfoVO processInfoVO = CommonCloneUtil.objectClone(processInfoBO, ProcessInfoVO.class);
        if (Objects.nonNull(processInfoBO) && !CollectionUtils.isEmpty(processInfoBO.getTaskList())) {
            processInfoVO.setTaskList(CommonCloneUtil.ListClone(processInfoBO.getTaskList(), TaskInfoVO.class));
        }
        return Result.success(processInfoVO);
    }

    @NonAuthority
    @Limits(value = {@Limit(count = 1)})
    @ApiOperation(value = "通过流程实例ID查询流程变量", notes = "通过流程实例ID查询流程变量")
    @GetMapping("/variables")
    @ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "String",
            paramType = "header")
    public Result<Map<String, Object>> variables(@RequestParam("processInstanceId") @ApiParam(value = "流程ID") String processInstanceId) {
        return Result.success(remoteProcessService.processVariables(processInstanceId));
    }

    @NonAuthority
    @Limits(value = {@Limit(count = 1)})
    @ApiOperation(value = "删除流程实例", notes = "删除流程实例")
    @PostMapping("/delete")
    @ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "String",
            paramType = "header")
    public Result<Boolean> delete(@RequestBody @Validated(ProcessFormDTO.Delete.class) ProcessFormDTO processFormDTO) {
        ProcessFormBO processFormBO = CommonCloneUtil.objectClone(processFormDTO, ProcessFormBO.class);
        processFormBO.setApplicant(AuthContext.getAuthUser().getUserId().toString());
        return Result.success(remoteProcessService.deleteProcess(processFormBO));
    }
}