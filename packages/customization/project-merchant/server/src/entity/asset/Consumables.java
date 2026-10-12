package com.guhecloud.cube.deliver.project.merchant.entity.asset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 易耗品
 * </p>
 *
 * @author bq
 * @since 2022-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("consumables")
public class Consumables implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 编号
     */
    private String code;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型ID
     */
    private String typeId;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 单位
     */
    private String unit;

    /**
     * 规格型号
     */
    private String model;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 价格
     */
    private Float price;

    /**
     * 仓库
     */
    private String warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 库存上限
     */
    private Integer inventoryUpLimit;

    /**
     * 库存下限
     */
    private Integer inventoryLowLimit;

    /**
     * 首次库存
     */
    private Integer firstInventory;

    /**
     * 当前库存
     */
    private Integer inventory;

    /**
     * 附加描述
     */
    private String remark;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;


}
