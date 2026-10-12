package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 密码修改参数
 *
 * @author Woody
 * @date 2021/7/21
 */
@Data
public class PasswordUpdateParam {
    /**
     * 登录密码
     */
    @NotEmpty(message = "登录密码不能为空")
    @Size(min = 6, max = 100, message = "登录密码长度不能小于6位且不能大于100位")
    private String password;

    /**
     * 新密码
     */
    @Size(min = 6, max = 100, message = "新密码长度不能小于6位且不能大于100位")
    private String newPassword;

    /**
     * 确认新密码
     */
    @Size(min = 6, max = 100, message = "确认密码长度不能小于6位且不能大于100位")
    private String secondNewPassword;
}
