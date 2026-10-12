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
 * 易耗品出入库记录
 * </p>
 *
 * @author bq
 * @since 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("consumables_stock")
public class ConsumablesStock implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 编号
     */
    private String code;

    /**
     * 出入库类型
     */
    private String type;

    /**
     * 仓库Id
     */
    private String warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 操作人ID
     */
    private String operatorId;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 使用人ID
     */
    private String userId;

    /**
     * 使用人名称
     */
    private String userName;

    /**
     * 使用部门ID
     */
    private String useDeptId;

    /**
     * 使用部门名称
     */
    private String useDeptName;

    /**
     * 出入库子类型
     */
    private String subType;

    /**
     * 出入库时间
     */
    private LocalDateTime date;

    /**
     * 备注
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
