package com.guhecloud.cube.biz.vmonitor.server.vo;

import com.guhecloud.cube.biz.vmonitor.server.param.AuthUserParam;
import lombok.Data;

import java.util.List;

@Data
public class SecurityDeviceAuthVO {
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 设备
     */
    private String deviceName;
    /**
     * 设备编码
     */
    private String code;
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 关联人员
     */
    private List<AuthUserParam> userList;

    private java.lang.String applicationCode;

    private java.lang.String brandName;
    private java.lang.String brandCode;
    private java.lang.String lntAndLat;
    private java.lang.String thirdPartyId;
    private java.lang.Boolean deviceStatus;
    private java.lang.String deviceProdCategoryCode;
    private java.lang.String deviceProdCategoryCodeConcat;
    private java.lang.String deviceProdCategoryName;
    private java.lang.Long deviceProdId;
    private java.lang.String deviceProdName;
}
