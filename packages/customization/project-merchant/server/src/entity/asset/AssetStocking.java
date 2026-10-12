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
 * 资产盘点
 * </p>
 *
 * @author bq
 * @since 2022-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("asset_stocking")
public class AssetStocking implements Serializable {

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
     * 名称
     */
    private String name;

    /**
     * 盘点人ID
     */
    private String userId;

    /**
     * 盘点人名称
     */
    private String username;

    /**
     * 计划开始时间
     */
    private LocalDateTime planBeginDate;

    /**
     * 计划结束时间
     */
    private LocalDateTime planEndDate;

    /**
     * 实际开始时间
     */
    private LocalDateTime realBeginDate;

    /**
     * 实际结束时间
     */
    private LocalDateTime realEndDate;

    /**
     * 盘点范围（全部、指定）
     */
    @TableField("`range`")
    private String range;

    /**
     * 指定范围类型的ID
     */
    private String targetId;

    /**
     * 状态
     */
    private String status;

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
