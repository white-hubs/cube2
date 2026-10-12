package com.guhecloud.cube.biz.mdata.client.dto;

import lombok.Data;

/**
 * 设备流媒体配置返回参数
 *
 * @author lanchun
 * @date 2021/12/7 11:08
 */
@Data
public class DeviceStreamMediaDTO {

    private Long id;

    /**
     * 设备主键
     */
    private Long deviceId;

    /**
     * 流媒体服务器配置主键
     */
    private Long steamMediaConfigId;

    /**
     * 流媒体服务器配置名
     */
    private String steamMediaName;

    /**
     * 流媒体服务器配置ip
     */
    private String steamMediaIpAddress;

    /**
     * 摄像机token
     */
    private String token;

    /**
     * 内网地址
     */
    private String steamMediaIntranetAddress;
}