package com.guhecloud.cube.base.device.center.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 设备实例属性扩展表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("center_biz_device_expand")
public class BizDeviceExpand extends BasicEnhancedTenantEntity<Long> {
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
     * 每日开机时长
     */
    private Integer dailyWorkTime;

    /**
     * 设备经纬度
     */
    private String lntAndLat;

    /**
     * 位置信息描述
     */
    private String locationDes;

    /**
     * 设备单价
     */
    private String unitPrice;

    /**
     * 采购日期
     */
    private LocalDate purchaseDate;

    /**
     * 生产日期
     */
    private LocalDate productionDate;

    /**
     * 设备安装图片
     */
    private Long picture;

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

    /**
     * 空间坐标信息
     */
    private String spatialCoordinates;

    private Boolean deleted;
}