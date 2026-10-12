package com.guhecloud.cube.iot.mgt.north.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryVo {
    private Long categoryId;
    private String categoryCode;
    private String categoryCodeConcat;
    private String categoryName;

    private List<ProductCategoryVo> children;

    private ProductCategoryVo parent;
}
