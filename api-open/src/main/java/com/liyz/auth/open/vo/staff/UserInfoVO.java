package com.liyz.auth.open.vo.staff;

import com.liyz.auth.common.base.desen.Desensitization;
import com.liyz.auth.common.base.desen.DesensitizationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 注释:
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2019/9/7 22:38
 */
@ApiModel(value = "UserInfoVO", description = "用户信息")
@Data
public class UserInfoVO implements Serializable {
    private static final long serialVersionUID = 4010688986098940232L;

    @ApiModelProperty(value = "用户id", example = "10001")
    private Long userId;

    @Desensitization(endIndex = 3)
    @ApiModelProperty(value = "用户名称", example = "user")
    private String loginName;

    @ApiModelProperty(value = "昵称", example = "啦啦啦")
    private String nickName;

    @Desensitization(DesensitizationType.REAL_NAME)
    @ApiModelProperty(value = "昵称", example = "张三")
    private String userName;

    @Desensitization(DesensitizationType.MOBILE)
    @ApiModelProperty(value = "手机号", example = "15988654789")
    private String mobile;

    @Desensitization(DesensitizationType.EMAIL)
    @ApiModelProperty(value = "邮箱", example = "example@google.com", allowEmptyValue = true)
    private String email;

    @ApiModelProperty(value = "角色信息")
    private List<Integer> roleIds;
}
