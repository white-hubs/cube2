package com.guhecloud.cube.biz.mdata.server.area.param;

import com.guhecloud.cube.biz.mdata.server.area.enumeration.VisualizationAreaTypeEnum;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * 区域更新信息
 */
@Data
public class AreaUpdateParam {

    /**
     * 主键
     */
    @NotNull(message = "主键不能空")
    @Min(1)
    private Long id;

    /**
     * 区域名称
     */
    @NotNull(message = "区域名称不能空")
    @Size(max = 100)
    private String name;

    /**
     * 区域别名
     */
    @Size(max = 100, message = "区域别名不能大于100")
    private String aliasName;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 占地面积
     */
    private BigDecimal builtUpArea;

    //区域管理id
    private String areaManagerId;

    //区域管理部门id
    private String areaDepartmentId;

    /**
     * 可视化区域类型
     */
    private VisualizationAreaTypeEnum visualizationAreaType;

    /**
     * 区域扩充配置
     */
    private AreaExtendParam areaExtend;

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
