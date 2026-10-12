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
 * 易耗品类型
 * </p>
 *
 * @author bq
 * @since 2022-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("asset_type")
public class AssetType implements Serializable {

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
     * 业务类型（资产、易耗品）
     */
    private String businessType;

    /**
     * 父级ID
     */
    private String pid;

    /**
     * 名称
     */
    private String name;

    /**
     * 单位
     */
    private String unit;

    /**
     * 排序
     */
    private int sort;

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
     * 删除标注
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;


}
