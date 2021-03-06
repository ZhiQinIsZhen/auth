package com.liyz.auth.open.controller.staff;

import com.liyz.auth.common.base.result.Result;
import com.liyz.auth.common.base.util.AuthContext;
import com.liyz.auth.common.base.util.CommonCloneUtil;
import com.liyz.auth.common.limit.annotation.Limit;
import com.liyz.auth.common.limit.annotation.Limits;
import com.liyz.auth.open.event.UserEvent;
import com.liyz.auth.open.vo.staff.UserInfoVO;
import com.liyz.auth.security.base.annotation.Anonymous;
import com.liyz.auth.security.base.annotation.NonAuthority;
import com.liyz.auth.service.staff.remote.RemoteRuleLogService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private RemoteRuleLogService remoteRuleLogService;
    @Autowired
    private ApplicationContext applicationContext;

    @PreAuthorize("hasAuthority('ALL;/user/info')")
    @Limits(value = {@Limit(count = 1)})
    @ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "String",
            paramType = "header")
    @ApiOperation(value = "获取登陆的用户信息", notes = "获取登陆的用户信息")
    @GetMapping("/info")
    public Result<UserInfoVO> info() {
        applicationContext.publishEvent(new UserEvent(this, AuthContext.getAuthUser().getUserId()));
        return Result.success(CommonCloneUtil.objectClone(AuthContext.getAuthUser(), UserInfoVO.class));
    }

    @NonAuthority
    @Limits(value = {@Limit(count = 1)})
    @ApiOperation(value = "获取登陆的用户ID", notes = "获取登陆的用户ID")
    @GetMapping("/id")
    @ApiImplicitParam(name = "Authorization", value = "认证token", required = true, dataType = "String",
            paramType = "header")
    public Result<Long> id() {
        applicationContext.publishEvent(new UserEvent(this, AuthContext.getAuthUser().getUserId()));
        String a = "a";
        a.concat("b");
        return Result.success(Objects.isNull(AuthContext.getAuthUser()) ? null : AuthContext.getAuthUser().getUserId());
    }

    @Anonymous
    @Limits(value = {@Limit(count = 1)})
    @ApiOperation(value = "创建数据", notes = "创建数据")
    @GetMapping("/insert")
    public Result<Boolean> insert() {
        remoteRuleLogService.insert("CN0001");
        return Result.success(Boolean.TRUE);
    }
}
