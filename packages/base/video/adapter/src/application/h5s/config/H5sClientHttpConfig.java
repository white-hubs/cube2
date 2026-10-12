package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.config;

import lombok.Data;

@Data
public class H5sClientHttpConfig {

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
     * 密码
     */
    private String pwd;

    /**
     * 账号
     */
    private String account;

    /**
     * 预览地址
     */
    private String previewReplaceUrl;

    /**
     * 视频预览地址端口
     */
    private String previewPort;

    /**
     * 抓图端口
     */
    private String snapCapturePort;
}
