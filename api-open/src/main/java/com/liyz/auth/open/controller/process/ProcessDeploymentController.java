package com.liyz.auth.open.controller.process;

import com.liyz.auth.common.base.result.PageResult;
import com.liyz.auth.common.base.result.Result;
import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.common.limit.annotation.Limit;
import com.liyz.auth.common.limit.annotation.Limits;
import com.liyz.auth.common.remote.page.Page;
import com.liyz.auth.open.dto.PageDTO;
import com.liyz.auth.open.vo.process.ProcessDefinitionVO;
import com.liyz.auth.security.base.annotation.NonAuthority;
import com.liyz.auth.service.process.bo.ProcessDefinitionBO;
import com.liyz.auth.service.process.bo.ProcessDeployBO;
import com.liyz.auth.service.process.constant.ProcessExceptionCodeEnum;
import com.liyz.auth.service.process.remote.RemoteProcessDeployService;
import io.swagger.annotations.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2021/8/27 14:01
 */
@Api(value = "流程图", tags = "流程图")
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
public class ProcessDeploymentController {

    @DubboReference
    private RemoteProcessDeployService remoteProcessDeployService;

    @SneakyThrows
    @NonAuthority
    @Limits(value = {@Limit(count = 1)})
    @ApiOperation(value = "流程图上传", notes = "流程图上传")
    @PostMapping("/deploy")
    @ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "String",
            paramType = "header")
    public Result<Boolean> deploy(@RequestParam(value = "file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".bpmn") && !fileName.endsWith(".zip")) {
            return Result.error(ProcessExceptionCodeEnum.FILE_PATTERN_ERROR);
        }
        ProcessDeployBO processDeployBO = new ProcessDeployBO();
        processDeployBO.setFileName(fileName);
        processDeployBO.setBytes(file.getBytes());
        remoteProcessDeployService.deploy(processDeployBO);
        return Result.success(Boolean.TRUE);
    }

    @NonAuthority
    @Limits(value = {@Limit(count = 1)})
    @ApiOperation(value = "流程图列表", notes = "流程图列表")
    @GetMapping("/deploy/list")
    @ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "String",
            paramType = "header")
    public Result<List<ProcessDefinitionVO>> deployList() {
        return Result.success(CommonCloneUtil.ListClone(remoteProcessDeployService.deployList(), ProcessDefinitionVO.class));
    }

    @NonAuthority
    @Limits(value = {@Limit(count = 1)})
    @ApiOperation(value = "分页查询流程图列表", notes = "分页查询流程图列表")
    @GetMapping("/deploy/page")
    @ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "String",
            paramType = "header")
    public PageResult<ProcessDefinitionVO> deployPage(PageDTO pageDTO) {
        Page<ProcessDefinitionBO> page = remoteProcessDeployService.deployPage(pageDTO.getPageNum(), pageDTO.getPageSize());
        return PageResult.success(CommonCloneUtil.pageClone(page, ProcessDefinitionVO.class));
    }
}
