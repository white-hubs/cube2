package com.guhecloud.cube.iot.mgt.north.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备表
 */
@Data
@TableName(value = "iot_dev_device")
public class DevDevice extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键id
     * -- GETTER --
     * 获取主键id
     *
     * @return id - 主键id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @TableField(value = "device_parent_id")
    private Long deviceParentId;

    /**
     * 设备KEY,后续扩展<br/>
     * 重复字段，后续删除
     * -- GETTER --
     * 获取设备KEY,唯一
     *
     * @return device_key - 设备KEY,唯一
     */
    @TableField(value = "device_key")
    private String deviceKey;

    /**
     * 编码,唯一
     * -- GETTER --
     * 获取编码,后续扩展
     *
     * @return device_number - 编码,后续扩展
     */
    @TableField(value = "device_number")
    private String deviceNumber;

    /**
     * 产品ID
     * -- GETTER --
     * 获取产品ID
     *
     * @return product_id - 产品ID
     */
    @TableField(value = "product_id")
    private Long productId;

    /**
     * 设备名称
     * -- GETTER --
     * 获取设备名称
     *
     * @return name - 设备名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 状态:0未激活;1在线;2离线;3已禁用
     * -- GETTER --
     * 获取状态:0未激活;1在线;2离线;3已禁用
     *
     * @return status - 状态:0未激活;1在线;2离线;3已禁用
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 设备类型:1真实设备; 0虚拟设备
     * -- GETTER --
     * 获取设备类型:1真实设备; 0虚拟设备
     *
     * @return device_type - 设备类型:1真实设备; 0虚拟设备
     */
    @TableField(value = "device_type")
    private Integer deviceType;

    /**
     * 激活时间
     */
    @TableField(value = "activation_time")
    private LocalDateTime activationTime;

    /**
     * 最近上线时间
     * -- GETTER --
     * 获取最近上线时间
     *
     * @return online_time - 最近上线时间
     */
    @TableField(value = "online_time")
    private LocalDateTime onlineTime;

    /**
     * 预留透明字段(可下发至设备的配置)
     * -- GETTER --
     * 获取预留透明字段(可下发至设备的配置)
     *
     * @return config - 预留透明字段(可下发至设备的配置)
     */
    @TableField(value = "config")
    private String config;

    /**
     * -- GETTER --
     *
     * @return raw_device_id
     */
    @TableField(value = "raw_device_id")
    private String rawDeviceId;

    @TableField(value = "auth_algorithm")
    private String authAlgorithm;

    @TableField(value = "salt")
    private String salt;
}