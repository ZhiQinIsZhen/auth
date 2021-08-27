package com.liyz.auth.open.controller.process;

import com.liyz.auth.common.base.result.Result;
import com.liyz.auth.common.limit.annotation.Limit;
import com.liyz.auth.common.limit.annotation.Limits;
import com.liyz.auth.security.base.annotation.NonAuthority;
import com.liyz.auth.security.client.AuthContext;
import com.liyz.auth.service.process.bo.ProcessDeployBO;
import com.liyz.auth.service.process.constant.ProcessExceptionCodeEnum;
import com.liyz.auth.service.process.remote.RemoteProcessDeployService;
import io.swagger.annotations.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

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
        log.info("file:{}, originalFilename:{}", file.getName(), file.getOriginalFilename());
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
}
