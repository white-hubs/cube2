package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.entity;

import lombok.Data;

/**
 * itc7700系列配置
 *
 * @author lanchun
 * @date 2024/11/7 9:54
 */
@Data
public class BaoLunVSevenSevenConfig {

    /**
     * 服务接口地址
     */
    private String serverApiAddr;

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
