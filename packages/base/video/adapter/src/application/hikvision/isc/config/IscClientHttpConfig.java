package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IscClientHttpConfig {
    //协议类型(http or https)
    private String schema;
    //平台的主机
    private String host;
    //平台端口
    private String port;
    //密钥appkey
    private String appKey;
    //密钥appSecret
    private String appSecret;

    private String pwd;

    private String account;

    /**
     * 播放地址
     */
    private String previewUrl;

    /**
     * 抓拍地址
     */
    private String snapCaptureUrl;

    /**
     * 版本号
     */
    private String version;
}
