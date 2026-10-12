package com.guhecloud.cube.biz.ouaa.server.third.wechat.vo;

import lombok.Data;

@Data
public class UserWechatVO {

    private Long id;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 微信openid
     */
    private String openId;

    /**
     * 微信unionid
     */
    private String unionId;
}
