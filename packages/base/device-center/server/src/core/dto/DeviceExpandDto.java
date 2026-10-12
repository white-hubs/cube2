package com.guhecloud.cube.base.device.center.core.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 设备业务属性扩展
 */
@Data
public class DeviceExpandDto {
    /**
     * 物联网设备ID
     */
    private Long id;
    /**
     * 设备秘钥的盐(占位)
     */
    private String securitySalt;

    /**
     * 设备安全密码
     */
    private String securityPwd;

    /**
     * 使用寿命
     */
    private Integer usefulLife;

    /**
     * 每日开机时长
     */
    private Integer dailyWorkTime;

    /**
     * 设备经纬度
     */
    private String lntAndLat;

    /**
     * 设备单价
     */
    private String unitPrice;

    /**
     * 采购日期
     */
    private LocalDate purchaseDate;

    /**
     * 设备图片地址
     */
    private Long picture;

    /**
     * 扩展表单配置(以JSON存放),eg: {tag_code:A001, input:"aaa"}
     */
    private String expandForm;

    /**
     * schema信息
     */
    private String expandSchema;

    /**
     * 安装单位
     */
    private String installUnit;

    /**
     * 安装电话
     */
    private String installPhone;

    /**
     * 安装日期
     */
    private LocalDate installDate;
}