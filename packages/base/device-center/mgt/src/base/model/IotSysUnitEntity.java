package com.guhecloud.cube.iot.mgt.base.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 单位表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "iot_sys_unit")
public class IotSysUnitEntity extends BasicEnhancedEntity<Long> {

    /**
     * 单位主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 单位code
     */
    @TableField(value = "unit_code")
    private String unitCode;

    /**
     * 单位名称
     */
    @TableField(value = "unit_name")
    private String unitName;

    /**
     * 单位类型
     */
    @TableField(value = "unit_type")
    private Integer unitType;

    /**
     * 状态0正常 1停用
     */
    @TableField(value = "unit_status")
    private Integer unitStatus;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

}