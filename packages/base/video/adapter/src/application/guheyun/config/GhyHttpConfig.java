package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.config;

import lombok.Data;

@Data
public class GhyHttpConfig {

    /**
     * 协议类型(http or https)
     */
    private String schema;

    /**
     * 主机地址
     */
    private String host;

    /**
     * 端口
     */
    private String port;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 播放地址
     */
    private String previewUrl;
}
