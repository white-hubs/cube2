package com.guhecloud.cube.biz.collection.analysis.gm.model.param;

import lombok.Data;

/**
 * 国家区域(三级)地址查询对象
 */
@Data
public class CountryAddressQueryParam {
    private Long id;
    private Long pid;
    private String name;
    private Long extId;
    private String extName;

    private String keyword;
    /**
     * 名称精确查询
     */
    private Boolean eqQuery;
    /**
     * 查询到第几层
     */
    private Integer deep;
}
