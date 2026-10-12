package com.guhecloud.cube.biz.ouaa.server.third.wechat.param.command;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class IncreaseUserWechatCommand {

    /**
     * 微信openid
     */
    @NotNull(message = "微信openid不能为空")
    private String openId;

    /**
     * 微信unionid
     */
    private String unionId;

    /**
     * 配置id
     */
    private Long configId;

}
