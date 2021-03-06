package com.liyz.auth.controller;

import com.liyz.auth.common.base.result.Result;
import com.liyz.auth.common.base.trace.annotation.Logs;
import com.liyz.auth.common.base.util.AuthContext;
import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.common.limit.annotation.Limit;
import com.liyz.auth.common.limit.annotation.Limits;
import com.liyz.auth.service.member.remote.RemoteUserInfoService;
import com.liyz.auth.vo.UserEventVO;
import com.liyz.auth.vo.UserInfoVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2020/1/6 14:31
 */
@Api(value = "用户信息", tags = "用户信息")
@ApiResponses(value = {
        @ApiResponse(code = 0, message = "成功"),
        @ApiResponse(code = 1, message = "失败"),
        @ApiResponse(code = 401, message = "暂无授权"),
        @ApiResponse(code = 404, message = "找不到资源"),
        @ApiResponse(code = 500, message = "服务器内部错误")
})
@Slf4j
@RestController
@RequestMapping("/user")
public class UserInfoController {

    @DubboReference
    private RemoteUserInfoService remoteUserInfoService;

    @Autowired
    private ApplicationContext applicationContext;

    @Logs
    @Limits(value = {@Limit(count = 1)})
    @ApiOperation(value = "获取登陆的用户ID", notes = "获取登陆的用户ID")
    @GetMapping("/id")
    @ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "String",
            paramType = "header")
    public Result<Long> id() {
        return Result.success(Objects.isNull(AuthContext.getAuthUser()) ? null : AuthContext.getAuthUser().getUserId());
    }

    @Logs
    @Limits(value = {@Limit(count = 1)})
    @ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "String",
            paramType = "header")
    @ApiOperation(value = "获取登陆的用户信息", notes = "获取登陆的用户信息")
    @GetMapping("/info")
    public Result<UserInfoVO> info() {
        applicationContext.publishEvent(new UserEventVO(this, AuthContext.getAuthUser().getUserId()));
        return Result.success(CommonCloneUtil.objectClone(AuthContext.getAuthUser(), UserInfoVO.class));
    }

    @Logs
    @Limits(value = {@Limit(count = 1)})
    @ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "String",
            paramType = "header")
    @ApiOperation(value = "测试事务同步器", notes = "测试事务同步器")
    @GetMapping("/testTransactionSynchronization")
    public Result<UserInfoVO> testTransactionSynchronization() {
        remoteUserInfoService.testTransactionSynchronization(AuthContext.getAuthUser().getUserId());
        return Result.success(CommonCloneUtil.objectClone(AuthContext.getAuthUser(), UserInfoVO.class));
    }

    @Logs
    @Limits(value = {@Limit(count = 1)})
    @ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "String",
            paramType = "header")
    @ApiOperation(value = "测试事务同步器-test", notes = "测试事务同步器-test")
    @GetMapping("/testTransactionSynchronization1")
    public Result<UserInfoVO> testTransactionSynchronization1() {
        remoteUserInfoService.testTransactionSynchronization1(AuthContext.getAuthUser().getUserId());
        return Result.success(CommonCloneUtil.objectClone(AuthContext.getAuthUser(), UserInfoVO.class));
    }
}
