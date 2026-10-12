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
 * 用户设备表
 * </p>
 *
 * @author zwb
 * @since 2020-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_device")
public class UserDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
