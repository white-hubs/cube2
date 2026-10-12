package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaoLunConfig {

    /**
     * 用户名称
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 过期时间(秒)
     */
    private Integer expireTime;

    /**
     * 引擎名称
     */
    private String ttsName;

    /**
     * 音量
     */
    private Integer volume;
}
