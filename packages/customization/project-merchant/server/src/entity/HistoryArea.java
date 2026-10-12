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
 * 空间、区域历史面积
 * </p>
 *
 * @author bq
 * @since 2021-07-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("history_area")
public class HistoryArea implements Serializable {

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
     * 对象类型(区域、空间)
     */
    private String objectType;

    /**
     * 对象ID
     */
    private String objectId;

    /**
     * 建筑面积
     */
    private float buildingArea;

    /**
     * 占地面积
     */
    private float coverArea;

    /**
     * 租赁面积
     */
    private float leaseArea;

    /**
     * 租赁实际面积
     */
    private float leaseRealArea;

    /**
     * 租赁公摊面积
     */
    private float leasePublicArea;

    /**
     * 消防面积
     */
    private float fireArea;

    /**
     * 清洁面积
     */
    private float cleanArea;

    /**
     * 车位面积
     */
    private float parkingArea;

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

}
