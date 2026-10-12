package com.guhecloud.cube.iot.mgt.north.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典类型值
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "iot_sys_dict_item")
public class SysDictItem extends BasicEnhancedTenantEntity<Long> {
    /**
     * 字典编码
     */
    private Long id;

    /**
     * 字典类型id
     */
    private Long dictId;

    /**
     * 字典上级Id
     */
    private Long itemParentId;

    /**
     * 字典排序
     */
    private Integer itemSort;

    /**
     * 层级,
     */
    private String itemLevel;

    /**
     * 字典标签
     */
    private String itemLabel;

    /**
     * 字典键值
     */
    private String itemValue;

    /**
     * 状态 0正常 1停用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 产品库中对应的ID
     */
    private Long adminId;
}