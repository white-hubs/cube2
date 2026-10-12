package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionProjectStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 消杀项目表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("disinfection_project")
public class DisinfectionProject extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目编号
     */
    @TableField("project_code")
    private String projectCode;

    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 项目类型ID
     */
    @TableField("project_type_id")
    private Long projectTypeId;

    /**
     * 项目类型名称
     */
    @TableField("project_type_name")
    private String projectTypeName;

    /**
     * 业主单位
     */
    private String company;

    /**
     * 项目开始时间
     */
    @TableField("begin_time")
    private LocalDateTime beginTime;

    /**
     * 项目结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 项目负责人ID
     */
    @TableField("leader_id")
    private Long leaderId;

    /**
     * 项目面积
     */
    @TableField("disinfection_area")
    private String disinfectionArea;
    /**
     * 项目所在地名称
     */
    private String disinfectionLocationName;
    /**
     * 项目所在地经纬度
     */
    @TableField("disinfection_location")
    private String disinfectionLocation;

    /**
     * 项目消杀范围经纬度，多个
     */
    @TableField("disinfection_scope")
    private String disinfectionScope;

    /**
     * 项目说明
     */
    private String description;

    /**
     * 项目状态
     */
    private DisinfectionProjectStatusEnum status;

    /**
     * 项目图片
     */
    private Long image;

    /**
     * 项目所在区
     */
    private String district;
}