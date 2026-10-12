package com.guhecloud.cube.base.iot.product.model.v3.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 物模型版本快照表v3版本(新增功能)
 * 对应表: iot_md_product_model_version_v3
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "iot_md_product_model_version_v3")
@Data
public class MdProductModelVersion extends BasicTenantEntity<Long> {

    /**
     * 版本ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 产品ID
     */
    @TableField(value = "prod_id")
    private Long prodId;

    /**
     * 产品编号
     */
    @TableField(value = "prod_number")
    private String prodNumber;

    /**
     * 版本号(格式: v1.0.0)
     */
    @TableField(value = "version_number")
    private String versionNumber;

    /**
     * 版本名称
     */
    @TableField(value = "version_name")
    private String versionName;

    /**
     * 完整的物模型JSON快照
     * 包含产品信息、所有属性、所有事件、所有方法的完整定义
     * 使用业务code而非ID,便于跨环境迁移
     */
    @TableField(value = "model_snapshot")
    private String modelSnapshot;

    /**
     * 变更说明
     */
    @TableField(value = "change_description")
    private String changeDescription;

    /**
     * 变更类型: create/update/delete
     */
    @TableField(value = "change_type")
    private String changeType;

    /**
     * 变更内容摘要(JSON格式)
     * 记录本次变更的简要信息,如: {"added":["prop1"],"modified":["event1"],"deleted":["method1"]}
     */
    @TableField(value = "change_summary")
    private String changeSummary;

    /**
     * 是否当前版本: 1是 0否
     */
    @TableField(value = "is_current")
    private Integer isCurrent;

    /**
     * 版本状态: 1正常 0已归档
     */
    @TableField(value = "status")
    private Integer status;
}
