package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备阈值
 * </p>
 *
 * @author bq
 * @since 2021-09-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("device_threshold_value")
public class DeviceThresholdValue implements Serializable {

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
     * 阈值模板名称
     */
    private String name;

    /**
     * 设备IDS
     */
    private String deviceIdStr;

    /**
     * 电压高限
     */
    private Integer dygxsz;

    /**
     * 电压低限
     */
    private Integer dydxsz;

    /**
     * 电流高限
     */
    private Integer dlgxsz;

    /**
     * 功率高限
     */
    private Integer glgxsz;

    /**
     * 漏电流高限
     */
    private Integer ldlgxsz;

    /**
     * 温度高限
     */
    private Integer wdgxsz;

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
     * 删除标志（0存在，1已删除）
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;


}
