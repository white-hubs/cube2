package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-07-22  10:14
 * @Description:
 * @Version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fireinsp_plan")
public class FireinspPlanPO implements Serializable {
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
     * 计划名称
     */
    private String name;

    /**
     * 生效时间
     */
    private LocalDateTime beginTime;

    /**
     * 失效时间
     */
    private LocalDateTime endTime;

    /**
     * 执行周期 周期类型:单次;每日;每周;每月
     */
    private String cycleType;

    /**
     * 执行日期
     */
    private String selection;

    /**
     * 执行时间
     */
    private LocalDateTime hour;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 部门名
     */
    private String deptName;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 巡检描述
     */
    private String description;

    /**
     * 启停状态
     */
    private Boolean enabled;

    /**
     * 评价人ID
     */
    private String reviewerId;

    /**
     * 任务超时时长（小时）
     */
    private Integer timeout;

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
     * 逻辑删除
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;
}
