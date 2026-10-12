package com.guhecloud.cube.biz.dispatch.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Appet
 * @Description 调度台登陆参数
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchLoginParam {

    //用户名
    @NotBlank
    private String userName;
    //密码
    @NotBlank
    private String password;

}