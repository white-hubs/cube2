package com.guhecloud.cube.base.device.center.metadata.std.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryDTO {

    private String categoryCode;
    private String categoryName;
    private String categoryCodeConcat;

    private List<ProductCategoryDTO> children;
}
