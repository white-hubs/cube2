package com.guhecloud.cube.biz.mdata.server.area.entity;

import com.guhecloud.cube.biz.mdata.server.area.enumeration.VisualizationAreaTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 区域信息
 */
@Data
public class Area extends BasicEnhancedTenantTreeEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域编号
     */
    private String code;

    /**
     * 占地面积
     */
    private BigDecimal builtUpArea;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 区域别名
     */
    private String aliasName;

    /**
     * 序号
     */
    private Integer sort;

    //区域管理id
    private String areaManagerId;

    //区域管理部门id
    private String areaDepartmentId;

    /**
     * 可视化区域类型
     */
    private VisualizationAreaTypeEnum visualizationAreaType;

    /**
     * 状态id
     */
    private Long statusId;
    /**
     * 特征ids
     */
    private String featureIds;
    /**
     * 标签ids
     */
    private String tagIds;
}
