package com.guhecloud.cube.biz.mdata.server.area.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 区域信息
 */
@Data
public class AreaVO extends Tree.Simple<Long, AreaVO> {

    /**
     * 主键
     */
    private Long id;

    private Integer sort;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域编号
     */
    private String code;

    /**
     * 序列号
     */
    private String sequence;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 占地面积
     */
    private BigDecimal builtUpArea;

    /**
     * 空间介绍
     */
    private String introduce;

    /**
     * 区域别名
     */
    private String aliasName;
    /**
     * 状态
     */
    private Long statusId;
    /**
     * 特征
     */
    private String featureIds;

    private String statusName;
    private String featureName;
}
