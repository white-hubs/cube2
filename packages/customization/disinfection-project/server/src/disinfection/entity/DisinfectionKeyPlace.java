package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 消杀重点场所实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("disinfection_key_place")
public class DisinfectionKeyPlace extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 场所名称
     */
    @TableField("place_name")
    private String placeName;

    /**
     * 场所分类ID
     */
    @TableField("place_type_id")
    private Long placeTypeId;

    /**
     * 场所分类名称
     */
    @TableField("place_type_name")
    private String placeTypeName;

    /**
     * 场所地址
     */
    private String placeAddress;

    /**
     * 省市区
     */
    private String province;
    private String city;
    private String region;

    /**
     * 重点场所详细位置
     */
    @TableField("place_detail")
    private String placeDetail;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 场所图片
     */
    private String imgs;

    /**
     * 场所说明
     */
    @TableField("place_description")
    private String placeDescription;

    /**
     * 项目所在区
     */
    private String district;
}