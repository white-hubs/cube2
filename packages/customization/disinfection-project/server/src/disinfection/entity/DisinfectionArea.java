package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消杀区域表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("disinfection_area")
public class DisinfectionArea extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 消杀项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 消杀区域名称
     */
    @TableField("area_name")
    private String areaName;

    /**
     * 消杀区域详情
     */
    @TableField("area_detail")
    private String areaDetail;
}