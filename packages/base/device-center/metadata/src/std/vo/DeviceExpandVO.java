package com.guhecloud.cube.base.device.center.metadata.std.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 设备实例属性扩展表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceExpandVO {
    /**
     * 设备ID
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
    private LocalDateTime purchaseDate;

    /**
     * 设备安装图片地址
     */
    private Long picture;

    /**
     * 生产日期
     */
    private LocalDateTime productionDate;

    /**
     * 空间坐标信息
     */
    private String spatialCoordinates;

    private String name;
}