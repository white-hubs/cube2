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
 * 资产调拨单
 * </p>
 *
 * @author bq
 * @since 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("asset_allocation")
public class AssetAllocation implements Serializable {

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
     * 一级区域ID
     */
    private String areaId1;

    /**
     * 二级区域ID
     */
    private String areaId2;

    /**
     * 三级区域ID
     */
    private String areaId3;

    /**
     * 申请人ID
     */
    private String applicantId;

    /**
     * 申请人名称
     */
    private String applicantName;

    /**
     * 批准人ID
     */
    private String approverId;

    /**
     * 批准人名称
     */
    private String approverName;

    /**
     * 转入部门ID
     */
    private String turnDeptId;

    /**
     * 转入部门名称
     */
    private String turnDeptName;

    /**
     * 调拨时间
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
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志(0代表存在，1代表已删除)
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;


}
