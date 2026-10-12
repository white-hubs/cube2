package com.guhecloud.cube.biz.mdata.client.dto;

import lombok.Data;

import java.util.List;

/**
 * @author lzy
 * @date 2021/11/17
 */
@Data
public class DeviceDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备编号
     */
    private String code;

    /**
     * 设备类型
     */
    private String type;

    /**
     * 所属区域
     */
    private List<String> areaNames;

    /**
     * 物联绑定
     */
    private Boolean iotBind;

    /**
     * 在线状态
     */
    private Boolean online;

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
     * 内网地址
     */
    private String steamMediaIntranetAddress;

    /**
     * 摄像机token
     */
    private String token;
    /**
     * 区域
     */
    private String area;
}
