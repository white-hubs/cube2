package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.config;

import lombok.Data;

@Data
public class UnisInsightHttpConfig {

    /**
     * 协议类型(http:// or https://)
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
     * 合作方用户id
     */
    private String clientId;

    /**
     * 合作方用户密码
     */
    private String clientSecret;

    /**
     * 播放地址替换域名
     */
    private String previewUrl;

    /**
     * 访问IP，用于平台判断访问者所在网域
     */
    private String visitIp;

    /**
     * URL有效时间，单位秒
     */
    private Integer keepAlive;

    /**
     * HLS/http-flv拉流端口类型
     */
    private Integer portType;

    /**
     * H265 HLS封装类型
     */
    private Integer h265MuxerType;
}
