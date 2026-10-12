package com.guhecloud.cube.biz.ouaa.server.third.wechat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户微信关联信息
 *
 * @author lanchun
 * @date 2025/1/10 14:59
 */
@Getter
@AllArgsConstructor
public class UserRefWechat {
    /**
     * 主键
     */
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

    /**
     * 微信配置id
     */
    private Long configId;

    /**
     * 租户编号
     */
    private Integer tenant;

    public UserRefWechat(Long id, Long userId, String openId, String unionId) {
        this.id = id;
        this.userId = userId;
        this.openId = openId;
        this.unionId = unionId;
    }
}
