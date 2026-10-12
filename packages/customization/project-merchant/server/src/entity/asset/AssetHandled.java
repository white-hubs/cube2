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
 * 资产处置单
 * </p>
 *
 * @author bq
 * @since 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("asset_handled")
public class AssetHandled implements Serializable {

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
     * 处置时间
     */
    private LocalDateTime date;

    /**
     * 处置方式
     */
    private String way;

    /**
     * 处置金额
     */
    private String money;

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
