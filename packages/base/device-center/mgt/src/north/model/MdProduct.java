package com.guhecloud.cube.iot.mgt.north.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品定义
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "iot_md_product")
@Data
public class MdProduct extends BasicEnhancedTenantEntity<Long> {
    /**
     * 产品ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 产品编号
     */
    @TableField(value = "prod_number")
    private String prodNumber;

    /**
     * 产品名称
     */
    @TableField(value = "prod_name")
    private String prodName;

    /**
     * 是否自定义品类
     */
    @TableField(value = "is_custom")
    private Integer isCustom;

    /**
     * 产品品类ID
     */
    @TableField(value = "prod_category_id")
    private Long prodCategoryId;

    /**
     * 节点类型: 1直连设备 2网关设备 3网关子设备
     */
    @TableField(value = "node_type")
    private Long nodeType;

    /**
     * 产品品牌id
     */
    @TableField(value = "prod_brand_id")
    private Long prodBrandId;

    /**
     * 接入方式Id
     */
    @TableField(value = "access_type_id")
    private Long accessTypeId;

    /**
     * 消息协议ID（非字典表）
     */
    @TableField(value = "protocol_id")
    private Long protocolId;

    /**
     * 产品描述
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 产品扩展字段,更多描述的可能
     */
    @TableField(value = "extend_config")
    private String extendConfig;

    /**
     * 产品状态: 1正常 0禁用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 产品库中对应的ID
     */
    @TableField(value = "admin_id")
    private Long adminId;

    @TableField(value = "prod_version")
    private String prodVersion;

}