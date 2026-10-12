package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 用户微信关联数据模型
 *
 * @author Woody
 * @date 2021/7/23
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserWechat implements Entity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户主键
     */
    private Long userId;
    /**
     * 微信公众号openid
     */
    private String openId;
    /**
     * web微信openid
     */
    private String webOpenid;
    /**
     * app微信openid
     */
    private String appOpenid;
    /**
     * 小程序微信openid
     */
    private String mpOpenid;

    /**
     * 微信unionid
     */
    private String unionId;

    private String accessToken;

    private String refreshToken;

    private String avatar;

    private String nickname;
}
