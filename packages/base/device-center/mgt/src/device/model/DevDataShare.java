package com.guhecloud.cube.iot.mgt.device.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备数据共享配置表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "iot_dev_data_share")
public class DevDataShare extends BasicEnhancedTenantEntity<Long> {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @TableField(value = "source_device_id")
    private Long sourceDeviceId;

    @TableField(value = "source_product_id")
    private Long sourceProductId;

    @TableField(value = "source_data_type")
    private Integer sourceDataType;

    @TableField(value = "source_data_id")
    private Long sourceDataId;

    @TableField(value = "source_data_code")
    private String sourceDataCode;

    @TableField(value = "target_device_id")
    private Long targetDeviceId;

    @TableField(value = "target_product_id")
    private Long targetProductId;

    @TableField(value = "target_data_id")
    private Long targetDataId;

    @TableField(value = "target_data_code")
    private String targetDataCode;

    @TableField(value = "share_mode")
    private Integer shareMode;

    @TableField(value = "extend_config")
    private String extendConfig;

    @TableField(value = "remark")
    private String remark;
}

