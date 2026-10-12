package com.guhecloud.cube.biz.mdata.server.area.vo;

import com.guhecloud.cube.biz.mdata.server.area.enumeration.VisualizationAreaTypeEnum;
import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 区域信息
 */
@Data
public class AreaListVO extends Tree.Simple<Long, AreaListVO> {

    private Long id;
    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域编码
     */
    private String code;

    /**
     * 序列号
     */
    private String sequence;

    /**
     * 区域别名
     */
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

    //区域管理ids
    private List<Long> areaManagerIds;

    //区域管理人员
    private List<String> areaManagerName = new ArrayList<>();

    /**
     * 可视化区域类型
     */
    private VisualizationAreaTypeEnum visualizationAreaType;

    /**
     * 区域扩展信息
     */
    private AreaExtendVO areaExtend;

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
    private String tagName;
    private String statusName;
    private String featureName;
}
